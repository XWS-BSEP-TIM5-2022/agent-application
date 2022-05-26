package com.xwsbsep.agent.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private Environment env;

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }


    public boolean sendAccountVerificationMail(String token, String emailTo, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailTo);
            mailMessage.setSubject("Account verification");
            mailMessage.setFrom(env.getProperty("spring.mail.username"));
//            mailMessage.setText("http://localhost:" + env.getProperty("frontend.port") + "/#/activateAccount?token=" + token + " \nTODO SD: mail message");
            mailMessage.setText("http://localhost:" + env.getProperty("server.port") + "/auth/activateAccount?token=" + token + " \nTODO SD: mail message");
            sendEmail(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
