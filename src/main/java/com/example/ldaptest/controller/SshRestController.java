package com.example.ldaptest.controller;

import com.example.ldaptest.models.form.SshForm;
import com.example.ldaptest.models.form.WinRmForm;
import com.example.ldaptest.service.SshService;
import com.example.ldaptest.service.WinRmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ssh")
@RequiredArgsConstructor
public class SshRestController {

    @Autowired
    private SshService sshService;

    @PostMapping("/powershell/run")
    public String runPowerShell(
            @RequestBody SshForm form
            ) {
        String response = sshService.runPowerShell(form.getPowerShellCommand());
        return response;
    }
}
