package com.example.ldaptest.models.form;

import lombok.Data;

import java.util.List;

@Data
public class IcaclsAuthForm {

    private String userId;

    private List<String> aclList;

    private String auth;
}
