package com.example.ldaptest.service;

import com.jcraft.jsch.*;
import io.cloudsoft.winrm4j.client.WinRmClientContext;
import io.cloudsoft.winrm4j.winrm.WinRmTool;
import io.cloudsoft.winrm4j.winrm.WinRmToolResponse;
import org.apache.http.client.config.AuthSchemes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

@Service
public class SshService {

    @Value("${ssh.host}")
    private String host;

    @Value("${ssh.port}")
    private int port;

    @Value("${ssh.username}")
    private String username;

    @Value("${ssh.password}")
    private String password;

    @Value("${ssh.timeout}")
    private int timeout;

    @Value("${ssh.sleeptime}")
    private long sleeptime;

    public static final Logger log = LogManager.getLogger(SshService.class);


    public String runPowerShell(String command) {
        StringBuilder output = new StringBuilder();

        JSch jSch = new JSch();
        try {
            Session session = jSch.getSession(username, host, port);
            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "no");
            session.setTimeout(timeout);
            session.connect();
            ChannelExec channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand("powershell -Command \"" + command + "\"");
            channel.setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();
            Thread.sleep(sleeptime);  // 결과 응답이 올때까지 대기

            byte[] buffer = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer);
                    if (i < 0) {
                        break;
                    }

                    output.append(new String(buffer, 0, i));
                }

                if (channel.isClosed()) {
                    break;
                }

                channel.disconnect();
                session.disconnect();
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("sshService::runPowerShell::output=" + output);

        return output.toString();
    }

}
