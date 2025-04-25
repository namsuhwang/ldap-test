package com.example.ldaptest.models.form;

import lombok.Data;

import java.util.List;

@Data
public class WinRmForm {

    private String powerShellCommand;

    private String dir;

    private String userId;

    private List<String> aclList;

    private String auth;

    private IcaclsAuthForm icaclsAuth;

    List<IcaclsAuthForm> icaclsAuthList;

    List<String> userIdList;
}
