package com.example.ldaptest.service;

import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.http.client.config.AuthSchemes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

}
