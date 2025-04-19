package com.example.ldaptest.models.mapper;

import com.example.ldaptest.models.dto.LdapGroup;
import com.example.ldaptest.models.dto.LdapGroupMember;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LdapGroupContextMapper implements ContextMapper<LdapGroup> {

    @Override
    public LdapGroup mapFromContext(Object ctx) throws javax.naming.NamingException {
        DirContextAdapter context = (DirContextAdapter) ctx;
        Attributes attrs = context.getAttributes();

        LdapGroup group = new LdapGroup();
        group.setDn(context.getDn().toString());
        group.setCn((String) attrs.get("cn").get());
        group.setGroupType((String) attrs.get("grouptype").get());

        group.setObjectClassList(new ArrayList<>());
        if(attrs.get("objectclass") != null && attrs.get("objectclass").size() > 0){
            group.getObjectClassList().addAll(
                Collections.list(attrs.get("objectclass").getAll())
                           .stream()
                           .map(Object::toString)
                           .collect(Collectors.toList())
            );
        }

        if (attrs.get("member") != null && attrs.get("member").size() > 0) {
            List<LdapGroupMember> ldapGroupMemberList = new ArrayList<>();
            for(int i = 0; i < attrs.get("member").size(); i++){
                ldapGroupMemberList.add(new LdapGroupMember((String) attrs.get("member").get(i)));
            }

            group.setMembers(ldapGroupMemberList);
        }

        return group;
    }

}
