package com.example.ldaptest.controller;

import com.example.ldaptest.models.dto.LdapGroup;
import com.example.ldaptest.models.dto.LdapUser;
import com.example.ldaptest.models.form.GroupForm;
import com.example.ldaptest.models.form.UserForm;
import com.example.ldaptest.service.LdapTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    테스트 데이터
        Active Directory :
                urls: ldap://192.168.1.101:389
                base: dc=albee,dc=com
                username: Administrator@albee.com
                password: dba3048!01
        보안그룹 : DevTeam01
        사용자1 : DevUser01 / dba3048!01
        사용자2 : DevUser02 / dba3048!01
        파일공유관리자 : FileShareAdmin / dba3048!01 Administrators그룹에 추가됨
 */
@RestController
@RequestMapping("/ldap/template")
public class LdapTemplateController {

    @Autowired
    private LdapTemplateService ldapTemplateService;

    @PostMapping("/user/get")
    public LdapUser getUser(
            @RequestBody UserForm userFrom
    ) {
        return ldapTemplateService.getAdUser(userFrom.getUserId());
    }

    @PostMapping("/user/get-all")
    public List<LdapUser> getUser(
    ) {
        return ldapTemplateService.getAdAllUsers();
    }

    @PostMapping("/user/get-list-by-name")
    public List<LdapUser> getUsers(
            @RequestBody UserForm userFrom
    ) {
        return ldapTemplateService.getAdUsers(userFrom.getUserName());
    }


    @PostMapping("/user/add")
    public boolean addUser(
            @RequestBody UserForm userFrom
    ) {
        boolean result =  ldapTemplateService.addAdUser(
                userFrom.getUid(),
                userFrom.getCn(),
                userFrom.getSn(),
                userFrom.getOu(),
                userFrom.getMail()
        );

        return result;
    }


    @PostMapping("/user/delete")
    public boolean delUser(
            @RequestBody UserForm userFrom
    ) {
        boolean result =  ldapTemplateService.deleteAdUser(userFrom.getUserId());

        return result;
    }


    @PostMapping("/group/get")
    public LdapGroup getGroup(
            @RequestBody GroupForm groupFrom
    ) {
        return ldapTemplateService.getAdGroup(groupFrom.getGroupId());
    }

    @PostMapping("/group/get-all")
    public List<LdapGroup> getAllGroups(
    ) {
        return ldapTemplateService.getAdAllGroups();
    }


    @PostMapping("/group/get-list-by-name")
    public List<LdapGroup> getGroups(
            @RequestBody GroupForm groupFrom
    ) {
        return ldapTemplateService.getAdGroups(groupFrom.getGroupName());
    }


    @PostMapping("/group/get-members")
    public List<String> getGroupMembers(
            @RequestBody GroupForm groupFrom
    ) {
        return ldapTemplateService.getAdGroupMembers(groupFrom.getGroupId(), groupFrom.getUserName());
    }

    @PostMapping("/group/add-member")
    public boolean addMember(
            @RequestBody GroupForm groupFrom
    ) {
        boolean result = ldapTemplateService.addUserForGroup(groupFrom.getGroupId(), groupFrom.getUserId());
        return result;

    }

    @PostMapping("/group/remove-member")
    public boolean delMember(
            @RequestBody GroupForm groupFrom
    ) {
        boolean result = ldapTemplateService.delUserFromGroup(groupFrom.getGroupId(), groupFrom.getUserId());
        return result;

    }
/*
    @PostMapping("/add/group-objectclass")
    public boolean addObjectClassForGroup(
            @RequestBody UserForm userFrom
    ) {
        boolean result = ldapTemplateService.addObjectClassForGroup(userFrom.getGroupId(), userFrom.getObjectClassName());
        return result;

    }*/


}
