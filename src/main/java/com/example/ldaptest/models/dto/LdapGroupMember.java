package com.example.ldaptest.models.dto;

import io.netty.util.internal.StringUtil;
import lombok.Data;

@Data
public class LdapGroupMember {
    /*
    member: "CN=Group Policy Creator Owners,CN=Users,DC=albee,DC=com"
     */
    private String cn;     // cn="Group Policy Creator Owners"
    private String info;   // info="CN=Group Policy Creator Owners,CN=Users,DC=albee,DC=com"

    public LdapGroupMember(String memberInfo) {
        if(!StringUtil.isNullOrEmpty(memberInfo)){
            this.info = memberInfo;

            String[] memberInfoArray = memberInfo.split(",");
            for(int i=0; i<memberInfoArray.length; i++){
                String[] items = memberInfoArray[i].split("=");
                for(int j=0; j<items.length; j++){
                    if(items[j].toLowerCase().equals("cn")){
                        this.cn = items[1]; // 첫번째 cn을 세팅. "CN=Group Policy Creator Owners,CN=Users,DC=albee,DC=com"
                        break;
                    }
                }
            }
        }
    }
}

