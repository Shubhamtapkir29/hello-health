package com.hellohealth.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    // ✅ Use environment variables (DevOps best practice)
    private static final String FROM_EMAIL = System.getenv().getOrDefault("MAIL_USER", "your_email@gmail.com");
    private static final String PASSWORD = System.getenv().getOrDefault("MAIL_PASS", "your_app_password");

    public static void sendEmail(String toEmail, String subject, String body) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            logger.info("Email sent successfully to {}", toEmail);

        } catch (MessagingException e) {
            logger.error("Failed to send email to {}", toEmail, e);
        }
    }
}
