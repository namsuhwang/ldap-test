package com.example.ldaptest.models.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class IcaclsObject {
    private String userNm;        // ALBEE\Administrator
    private String targetPath;    // c:/FileServer
    private Boolean oiYn;         // "(OI)" = 하위 파일에 상속함
    private Boolean ciYn;         // "(CI)" = 하위 폴더에 상속함
    private Boolean ioYn;         // "(IO)" = 상속 전용, 현재 객체에는 적용되지 않음
    private Boolean npYn;         // "(NP)" = 하위 객체에는 적용하되 더 아래로는 상속하지 않음
    private Boolean idYn;         // "(ID)" = 상속된 ACE, 수동으로 설정한 것이 아님 (읽기 전용 표시)
    private String auth;          // 권한

    // ALBEE\Administrator:(OI)(CI)(F)
    public IcaclsObject(String path, String icaclsStr, Matcher matcherInput) {
        if(!StringUtils.hasText(icaclsStr)) {
            return;
        }

        if(!StringUtils.hasText(icaclsStr)) {
            return;
        }

        this.targetPath = path;
        this.oiYn = false;
        this.ciYn = false;
        this.ioYn = false;
        this.npYn = false;
        this.idYn = false;

        String[] split = icaclsStr.split(":");
        this.userNm = split[0];

        Matcher matcher = null;
        if(matcherInput != null) {
            matcher = matcherInput;
        }else{
            String authStr = split[1];

            // 정규식 패턴: 괄호 안의 값들을 추출
            Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
            matcher = pattern.matcher(authStr);
        }

        while (matcher.find()) {
            String authVal = matcher.group(1); // 괄호 안의 값

            if(authVal != null) {
                switch (authVal) {
                    case "OI": this.oiYn = true; break;
                    case "CI": this.ciYn = true; break;
                    case "IO": this.ioYn = true; break;
                    case "NP": this.npYn = true; break;
                    case "ID": this.idYn = true; break;

                    case "F", "M", "RX", "R", "W", "L", "D", "N": this.auth = authVal; break;
                    default: break;
                }
            }
        }
    }

    /* 권한부여 명령어 생성
     icacls "D:\Shared\Docs" /grant "username:(OI)(CI)RX"
    */
    public String getGrantString(String dirAuth){
        String authStr = (oiYn ?  "(OI)": "")
                + (ciYn ? "(CI)" : "")
                + (ioYn ? "(IO)" : "")
                + (npYn ? "(NP)" : "")
                + (idYn ? "(ID)" : "")
                + "(" + dirAuth + ")";

        String grantStr = "icacls ";
        grantStr += "\"" + targetPath + "\"" + "/grant ";
        grantStr += "\"" + userNm + ":" + authStr + "\"";
        return grantStr;
    }
}

