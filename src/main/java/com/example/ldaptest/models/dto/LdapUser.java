package com.example.ldaptest.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class LdapUser {
    private String dn;
    private String cn;
    private String sn;
    private String ou;
    private String uid;
    private String dc;
    private String base;
    private String mail;
    private List<String> objectClassList;
//    private String givenname;
//    private String userpassword;
//    private String ou;
//    private String objectclass;
//    private String description;


    /*
    "givenname" -> {LdapAttribute@11989} "givenName: Amy"
    "sn" -> {LdapAttribute@11991} "sn: Kroker"
    "userpassword" -> {LdapAttribute@11993} "userPassword: [B@16fac32c"
    "ou" -> {LdapAttribute@11995} "ou: Intern"
    "mail" -> {LdapAttribute@11997} "mail: amy@planetexpress.com"
    "objectclass" -> {LdapAttribute@11999} "objectClass: top, person, organizationalPerson, inetOrgPerson"
    "uid" -> {LdapAttribute@12001} "uid: amy"
    "cn" -> {LdapAttribute@12003} "cn: Amy Wong"
    "description" -> {LdapAttribute@12005} "description: Human"
            */
}

