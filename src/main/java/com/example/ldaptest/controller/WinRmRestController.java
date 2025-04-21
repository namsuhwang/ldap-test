package com.example.ldaptest.controller;

import com.example.ldaptest.models.form.WinRmForm;
import com.example.ldaptest.service.WinRmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String response = winRmService.runPowerShell(form.getRmCommand());
        return response;
    }
}
