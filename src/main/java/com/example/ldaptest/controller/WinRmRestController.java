package com.example.ldaptest.controller;

import com.example.ldaptest.models.dto.IcaclsObject;
import com.example.ldaptest.models.form.IcaclsAuthForm;
import com.example.ldaptest.models.form.WinRmForm;
import com.example.ldaptest.service.WinRmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/winrm")
@RequiredArgsConstructor
public class WinRmRestController {

    @Autowired
    private WinRmService winRmService;

    @PostMapping("/powershell/run")
    public String runPowerShell(
            @RequestBody WinRmForm form
            ) {
        String response = winRmService.runPowerShell(form.getPowerShellCommand());
        return response;
    }

    @PostMapping("/powershell/icacls/grant/one")
    public Boolean runIcaclsGrantOne(
            @RequestBody WinRmForm form
            ) {
        IcaclsAuthForm authForm = new IcaclsAuthForm();
        authForm.setAuth(form.getAuth());
        authForm.setUserId(form.getUserId());
        authForm.setAclList(form.getAclList());
        boolean result = winRmService.runIcaclsGrantOne(form.getDir(), authForm);
        return result;
    }

    @PostMapping("/powershell/icacls/grant/multi")
    public Boolean runIcaclsGrantMulti(
            @RequestBody WinRmForm form
            ) {
        boolean result = winRmService.runIcaclsGrantMulti(form.getDir(), form.getIcaclsAuthList());
        return result;
    }


    @PostMapping("/powershell/icacls/remove/one")
    public Boolean runIcaclsRemoveOne(
            @RequestBody WinRmForm form
            ) {
        boolean result = winRmService.runIcaclsRemoveOne(form.getDir(), form.getUserId());
        return result;
    }

    @PostMapping("/powershell/icacls/remove/multi")
    public Boolean runIcaclsRemoveMulti(
            @RequestBody WinRmForm form
            ) {
        boolean result = winRmService.runIcaclsRemoveMulti(form.getDir(), form.getUserIdList());
        return result;
    }

    @PostMapping("/get/path-member-list")
    public List<IcaclsObject> getPathMemberList(
            @RequestBody WinRmForm form
            ) {
        List<IcaclsObject> result = winRmService.getDirMemberAuthList(form.getDir());
        return result;
    }


}
