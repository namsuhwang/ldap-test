package com.example.ldaptest.models.dto;

import io.netty.util.internal.StringUtil;
import lombok.Data;

@Data
public class LdapGroupMember {
    /*
    member: cn=Philip J. Fry,ou=people,dc=planetexpress,dc=com
     */
    private String cn;     // cn=Philip J. Fry
    private String info;   // cn=Philip J. Fry,ou=people,dc=planetexpress,dc=com

    public LdapGroupMember(String memberInfo) {
        if(!StringUtil.isNullOrEmpty(memberInfo)){
            this.info = memberInfo;

            String[] memberInfoArray = memberInfo.split(",");
            for(int i=0; i<memberInfoArray.length; i++){
                String[] items = memberInfoArray[i].split("=");
                for(int j=0; j<items.length; j++){
                    if(items[j].equals("cn")){
                        this.cn = items[1];
                    }
                }
            }
        }
    }
}

