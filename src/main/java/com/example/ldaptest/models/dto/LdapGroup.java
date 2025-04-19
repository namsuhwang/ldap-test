package com.example.ldaptest.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class LdapGroup {
    private String dn;
    private String groupType;
    private String cn;
    private List<String> objectClassList;
    private List<LdapGroupMember> members;
}

