package com.example.ldaptest.controller;

import com.example.ldaptest.models.entity.LdapUserEntity;
import com.example.ldaptest.models.form.UserForm;
import com.example.ldaptest.service.LdapUserRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ldap/repo/user")
@RequiredArgsConstructor
public class LdapUserRepoController {

    @Autowired
    private LdapUserRepoService ldapUserRepoService;

    // id = dn
    @PostMapping("/get-by-id")
    public LdapUserEntity getUser(
            @RequestBody UserForm userFrom
    ) {
        Optional<LdapUserEntity> user = ldapUserRepoService.getUser(userFrom.getUserId());
        return user.orElse(null);
    }

    // name = cn
    @PostMapping("/get-list-by-name")
    public List<LdapUserEntity> getUsersByName(
            @RequestBody UserForm userFrom
    ) {
        List<LdapUserEntity> userList = ldapUserRepoService.getUsersByName(userFrom.getUserName());
        return userList;
    }

    @PostMapping("/add")
    public boolean createUser(
            @RequestBody UserForm userFrom
    ) {
        boolean result = ldapUserRepoService.addUser(
                userFrom.getUid(),
                userFrom.getCn(),
                userFrom.getSn(),
                userFrom.getOu(),
                userFrom.getMail()
        );
        return result;
    }


    @PostMapping("/remove")
    public boolean deleteUser(
            @RequestBody UserForm userFrom
    ) {
        boolean result = ldapUserRepoService.deleteUser(userFrom.getDn());
        return result;
    }
}
