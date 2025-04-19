package com.example.ldaptest.models.mapper;

import com.example.ldaptest.models.dto.LdapGroup;
import com.example.ldaptest.models.dto.LdapGroupMember;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;

public class LdapGroupAttributesMapper implements AttributesMapper<LdapGroup> {
    @Override
    public LdapGroup mapFromAttributes(Attributes attrs) throws javax.naming.NamingException {
        LdapGroup group = new LdapGroup();
        group.setCn((String) attrs.get("cn").get());
        group.setGroupType((String) attrs.get("grouptype").get());
        // group.setObjectClass((String) attrs.get("objectclass").get());
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
