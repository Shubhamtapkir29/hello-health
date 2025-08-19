package util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";   // or your SMTP server
    private static final String SMTP_PORT = "587";              // 587 for TLS, 465 for SSL
    private static final String USERNAME = "your_email@gmail.com";  // your Gmail ID
    private static final String PASSWORD = "your_app_password";     // App password, not real password!

    /**
     * Sends an email
     * @param toEmail Recipient's email
     * @param subject Subject of the email
     * @param body Body of the email
     */
    public static void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();

        // SMTP server config
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        // Create a session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME, "Hello Health"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send
            Transport.send(message);
            System.out.println("✅ Email sent successfully to " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send email to " + toEmail);
        }
    }
}
