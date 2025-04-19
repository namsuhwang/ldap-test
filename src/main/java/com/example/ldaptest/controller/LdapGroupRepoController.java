package com.example.ldaptest.controller;

import com.example.ldaptest.models.entity.LdapGroupEntity;
import com.example.ldaptest.models.entity.LdapUserEntity;
import com.example.ldaptest.models.form.GroupForm;
import com.example.ldaptest.models.form.UserForm;
import com.example.ldaptest.service.LdapGroupRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ldap/repo/group")
@RequiredArgsConstructor
public class LdapGroupRepoController {

    @Autowired
    private LdapGroupRepoService ldapGroupRepoService;

    // id = dn
    @PostMapping("/get-by-id")
    public LdapGroupEntity getGroup(
            @RequestBody GroupForm groupForm
    ) {
        Optional<LdapGroupEntity> group = ldapGroupRepoService.getGroup(groupForm.getGroupId());
        return group.orElse(null);
    }

    // name = cn
    @PostMapping("/get-list-by-name")
    public List<LdapGroupEntity> getGroupsByName(
            @RequestBody GroupForm groupForm
    ) {
        List<LdapGroupEntity> groupList = ldapGroupRepoService.getGroupsByName(groupForm.getGroupName());
        return groupList;
    }

    @PostMapping("/add")
    public boolean createGroup(
            @RequestBody GroupForm groupForm
    ) {
        boolean result = ldapGroupRepoService.addGroup(
                groupForm.getCn(),
                groupForm.getGroupType()
        );
        return result;
    }

    @PostMapping("/add-member")
    public boolean addMemberForGroup(
            @RequestBody GroupForm groupForm
    ) {
        boolean result = ldapGroupRepoService.addUserForGroup(groupForm.getGroupId(), groupForm.getUserId());
        return result;
    }

    @PostMapping("/remove-member")
    public boolean removeMemberForGroup(
            @RequestBody GroupForm groupForm
    ) {
        boolean result = ldapGroupRepoService.removeUserFromGroup(groupForm.getGroupId(), groupForm.getUserId());
        return result;
    }


    /*
    @PostMapping("/remove")
    public boolean deleteGroup(
            @RequestBody GroupForm groupForm
    ) {
        boolean result = ldapGroupRepoService.deleteUser(groupForm.getDn());
        return result;
    }*/
}
