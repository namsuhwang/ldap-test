package com.example.ldaptest.service;

import com.example.ldaptest.models.dto.IcaclsObject;
import com.example.ldaptest.models.form.IcaclsAuthForm;
import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.http.client.config.AuthSchemes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WinRmService {

    @Value("${winrm.host}")
    private String host;

    @Value("${winrm.port}")
    private int port;

    @Value("${winrm.username}")
    private String username;

    @Value("${winrm.password}")
    private String password;

    @Value("${winrm.timeout}")
    private String timeout;


    public static final Logger log = LogManager.getLogger(WinRmService.class);


    public String runPowerShell(String command) {

        // WinRM 클라이언트 컨텍스트 생성
        WinRmClientContext context = WinRmClientContext.newInstance();

        // WinRmTool 빌더를 사용하여 인스턴스 생성
        WinRmTool tool = WinRmTool.Builder.builder(host, username, password)
                .authenticationScheme(AuthSchemes.NTLM)
                .port(port)
                .useHttps(false)
                .disableCertificateChecks(true)
                .context(context)
                .build();

        // 명령 실행
        WinRmToolResponse response = tool.executePs(command);

        // 결과 출력
        System.out.println("=== STDOUT ===");
        System.out.println(response.getStdOut());

        System.out.println("=== STDERR ===");
        System.out.println(response.getStdErr());

        System.out.println("=== EXIT CODE ===");
        System.out.println(response.getStatusCode());

        // 컨텍스트 종료
        context.shutdown();

        String result = StringUtils.hasText(response.getStdOut()) ? response.getStdOut() : "error or null";

        return result;
    }

    public List<IcaclsObject> getDirMemberAuthList(String dir) {
        // "icacls c:/FileServer "
        String command = "icacls " + dir;
        WinRmToolResponse response = runCommand(command);

        List<IcaclsObject> icaclsObjectList = new ArrayList<>();

        if(response.getStatusCode() == 0) {
            String responseStr = response.getStdOut();
            responseStr = StringUtils.hasText(responseStr) ? responseStr.trim() : "error or null";
            int idx01 = responseStr.indexOf(" ");
            String dirPath = responseStr.substring(0, idx01);
            String icaclsStr = responseStr.trim().substring(idx01 + 1);
            log.info("dirPath:" + dirPath);
            log.info("icaclsStr:" + icaclsStr);

            String[] icaclsAry = icaclsStr.split("\n");
            if(icaclsAry != null && icaclsAry.length > 0) {
                for(String icacls : icaclsAry) {
                    if(!StringUtils.hasText(icacls)) {
                        continue;
                    }

                    // 정규식 패턴: 괄호 안의 값들을 추출
                    Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
                    Matcher matcher = pattern.matcher(icacls.trim());
                    if(matcher.find()) {
                        log.info("matched::icacls:" + icacls);
                        IcaclsObject io = new IcaclsObject(dirPath, icacls.trim(), pattern.matcher(icacls.trim()));
                        if(io != null) {
                            icaclsObjectList.add(io);
                        }
                    }
                }
            }
        }else{
            log.error("Error while executing response: " + response.getStatusCode() + "::" + response.getStdErr());
        }

        return icaclsObjectList;
    }

    public boolean runIcaclsGrantOne(String dir, IcaclsAuthForm icaclsAuth) {
        if(!StringUtils.hasText(dir) || icaclsAuth == null) {
            log.error("Error input parameters");
            return false;
        }

        String command = "icacls " + "\"" + dir + "\"" + " /grant \"" + icaclsAuth.getUserId() + ":";

        String authStr = "";
        if(!StringUtils.hasText(icaclsAuth.getAuth()) || icaclsAuth.getAclList() == null || icaclsAuth.getAclList().size() == 0) {
            log.error("Error input parameters::IcaclsAuthForm");
            return false;
        }

        for(String acl : icaclsAuth.getAclList()) {
            authStr += "(" + acl + ")";
        }

        authStr +=  "(" + icaclsAuth.getAuth() + ")";

        command += authStr + "\"";

        log.info("command:" + command);

        WinRmToolResponse response = runCommand(command);

        boolean result = true;
        if(response.getStatusCode() != 0) {
            log.error("Error while executing response: " + response.getStatusCode() + "::" + response.getStdErr());
            return false;
        }

        return result;
    }

    public boolean runIcaclsGrantMulti(String dir, List<IcaclsAuthForm> icaclsAuthList) {
        if(!StringUtils.hasText(dir) || icaclsAuthList == null || icaclsAuthList.size() == 0) {
            log.error("Error input parameters");
            return false;
        }

        if(icaclsAuthList.size() > 100){
            log.error("Error input parameters::100");
            return false;
        }

        StringBuilder command = new StringBuilder();
        command.append("icacls " + "\"" + dir + "\"");

        for(IcaclsAuthForm authForm : icaclsAuthList) {
            String authStr = " /grant \"" + authForm.getUserId() + ":";
            if(!StringUtils.hasText(authForm.getAuth()) || authForm.getAclList() == null || authForm.getAclList().size() == 0) {
                log.error("Error input parameters::IcaclsAuthForm");
                return false;
            }

            for(String acl : authForm.getAclList()) {
                authStr += "(" + acl + ")";
            }

            authStr += "(" + authForm.getAuth() + ")";

            command.append(authStr + "\"");
        }

        log.info("command:" + command.toString());

        WinRmToolResponse response = runCommand(command.toString());

        boolean result = true;
        if(response.getStatusCode() != 0) {
            log.error("Error while executing response: " + response.getStatusCode() + "::" + response.getStdErr());
            return false;
        }

        return result;
    }

    public boolean runIcaclsRemoveOne(String dir, String userId) {
        if(!StringUtils.hasText(dir) || !StringUtils.hasText(userId) ) {
            log.error("Error input parameters");
            return false;
        }

        String command = "icacls " + "\"" + dir + "\"" + " /remove \"" + userId + "\"";

        log.info("command:" + command);

        WinRmToolResponse response = runCommand(command.toString());

        boolean result = true;
        if(response.getStatusCode() != 0) {
            log.error("Error while executing response: " + response.getStatusCode() + "::" + response.getStdErr());
            return false;
        }

        return result;
    }

    public boolean runIcaclsRemoveMulti(String dir, List<String> userIdList) {
        if(!StringUtils.hasText(dir) || userIdList == null || userIdList.size() == 0) {
            log.error("Error input parameters");
            return false;
        }

        StringBuilder command = new StringBuilder();
        command.append("icacls " + "\"" + dir + "\"");

        for(String userId : userIdList) {
            command.append(" /remove \"" + userId + "\"");
        }

        log.info("command:" + command.toString());

        WinRmToolResponse response = runCommand(command.toString());

        boolean result = true;
        if(response.getStatusCode() != 0) {
            log.error("Error while executing response: " + response.getStatusCode() + "::" + response.getStdErr());
            return false;
        }

        return result;
    }


    private WinRmToolResponse runCommand(String command) {

        // WinRM 클라이언트 컨텍스트 생성
        WinRmClientContext context = WinRmClientContext.newInstance();

        // WinRmTool 빌더를 사용하여 인스턴스 생성
        WinRmTool tool = WinRmTool.Builder.builder(host, username, password)
                .authenticationScheme(AuthSchemes.NTLM)
                .port(port)
                .useHttps(false)
                .disableCertificateChecks(true)
                .context(context)
                .build();

        // 명령 실행
        WinRmToolResponse response = tool.executePs(command);

        // 결과 출력
        System.out.println("=== STDOUT ===");
        System.out.println(response.getStdOut());

        System.out.println("=== STDERR ===");
        System.out.println(response.getStdErr());

        System.out.println("=== EXIT CODE ===");
        System.out.println(response.getStatusCode());

        // 컨텍스트 종료
        context.shutdown();
        return response;
    }

}
