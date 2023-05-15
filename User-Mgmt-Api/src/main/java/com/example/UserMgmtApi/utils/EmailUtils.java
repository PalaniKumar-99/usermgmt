package com.example.UserMgmtApi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;
    private Logger logger = LoggerFactory.getLogger(EmailUtils.class);
    public boolean sendEmail(String to, String subject, String body) {
        boolean isMailSent = false;
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            isMailSent = true;

        } catch (Exception e) {
            logger.error("Error Occured", e);
        }
        return isMailSent;
    }
}
