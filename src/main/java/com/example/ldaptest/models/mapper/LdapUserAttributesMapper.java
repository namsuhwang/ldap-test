package com.example.ldaptest.models.mapper;

import com.example.ldaptest.models.dto.LdapUser;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.directory.Attributes;

public class LdapUserAttributesMapper implements AttributesMapper<LdapUser> {
    @Override
    public LdapUser mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
        LdapUser user = new LdapUser();
        user.setUid((String) attrs.get("uid").get());
        user.setCn((String) attrs.get("cn").get());
        user.setOu((String) attrs.get("ou").get());
        if (attrs.get("mail") != null) {
            user.setMail((String) attrs.get("mail").get());
        }
//        user.setGivenname((String) attrs.get("givenname").get());
//        user.setUserpassword((String) attrs.get("userpassword").get());
//        user.setOu((String) attrs.get("ou").get());
//        user.setObjectclass((String) attrs.get("objectclass").get());
//        user.setDescription((String) attrs.get("description").get());
        return user;

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
}
