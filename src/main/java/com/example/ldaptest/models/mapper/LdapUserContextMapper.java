package com.example.ldaptest.models.mapper;

import com.example.ldaptest.common.LdapUtil;
import com.example.ldaptest.models.dto.LdapUser;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.naming.directory.Attributes;
import java.util.ArrayList;

public class LdapUserContextMapper implements ContextMapper<LdapUser> {

    @Override
    public LdapUser mapFromContext(Object ctx) throws javax.naming.NamingException {
        DirContextAdapter context = (DirContextAdapter) ctx;
        Attributes attrs = context.getAttributes();

        LdapUser user = new LdapUser();
        user.setDn(context.getDn().toString());
        user.setCn((String) attrs.get("cn").get());
        user.setOu(attrs.get("ou") != null ? (String) attrs.get("ou").get() : null);
        user.setSn((attrs.get("sn") != null ? (String) attrs.get("sn").get() : null));
        user.setUid((attrs.get("uid") != null ? (String) attrs.get("uid").get() : null));
        user.setMail(attrs.get("mail") != null ? (String) attrs.get("mail").get() : null);
        user.setDc(LdapUtil.extractDcFromContext(context));

        user.setObjectClassList(new ArrayList<>());
        if(attrs.get("objectclass") != null && attrs.get("objectclass").size() > 0){
            user.getObjectClassList().addAll(
                Collections.list(attrs.get("objectclass").getAll())
                           .stream()
                           .map(Object::toString)
                           .collect(Collectors.toList())
            );
        }
        return user;
    }



}
