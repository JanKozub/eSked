package org.jk.eSked.services.emailService;

import org.jk.eSked.services.emailService.DB.DBEmailInterface;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService implements EmailInterface {
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;

    @Override
    public void sendNewUserEmail(String email, String username, DBEmailInterface dbEmailInterface) throws MessagingException {
        EmailData emailData = null;
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", emailData.getStmpPort());
        mailServerProperties.put("mail.smtp.auth", emailData.getStmpAuth());
        mailServerProperties.put("mail.smtp.starttls.enable", emailData.getSmtpStarttlsEnable());

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
        generateMailMessage.setSubject("Welcome to eSked community!");

        String emailBody = "some text in future";

        generateMailMessage.setContent(emailBody, "text/html");
        Transport transport = getMailSession.getTransport("smtp");
        transport.connect(emailData.getHost(), emailData.getEmail(), emailData.getPassword());
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    @Override
    public void sendForgotPasswordEmail(String email, String username, int code) {

    }
}
