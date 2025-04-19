package com.example.ldaptest.models.form;

import lombok.Data;

@Data
public class GroupForm {

    private String groupId;
    private String userId;
    private String userName;
    private String groupName;
    private String groupType;
    private String dn;
    private String cn;
    private String sn;
    private String ou;
    private String uid;
    private String mail;
    private String objectClassName;
}
