package com.example.ldaptest.common;

import org.springframework.ldap.core.DirContextOperations;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;

public class LdapUtil {

    public static String extractDcFromContext(DirContextOperations ctx) {
        String dn = ctx.getDn().toString();  // 예: "uid=user1,ou=people,dc=example,dc=com"
        String[] parts = dn.split(",");
        StringBuilder dcBuilder = new StringBuilder();


        for (String part : parts) {
            if (part.startsWith("dc=")) {
                if (dcBuilder.length() > 0) {
                    dcBuilder.append(".");
                }
                dcBuilder.append(part.substring(3));
            }
        }

        return dcBuilder.toString();  // 예: "example.com"
    }

    public static LdapName getLdapNameFromDnString(String dn){
        LdapName ldapName = null;
        try {
            ldapName = new LdapName(dn);
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        }

        return ldapName;
    }

}
