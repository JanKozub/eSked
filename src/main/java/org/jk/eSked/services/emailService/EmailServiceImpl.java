package org.jk.eSked.services.emailService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    private final Properties mailServerProperties;
    private final Session getMailSession;

    public EmailServiceImpl(@Value("smtp.host") String smtpHost, @Value("smtp.port") String smtpPort, @Value("smtp.auth") String smtpAuth, @Value("smtp.starttls") String smtpStarttls) {
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", smtpPort);
        mailServerProperties.put("mail.smtp.auth", smtpAuth);
        mailServerProperties.put("mail.smtp.starttls.enable", smtpStarttls);

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
    }

    @Override
    public void sendNewUserEmail(String email, String username) throws MessagingException {

        // call smtp client
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
        generateMailMessage.setSubject("Welcome to eSked community!");


        String emailBody = "some text in future";


        generateMailMessage.setContent(emailBody, "text/html");
        Transport transport = getMailSession.getTransport("smtp");
        //transport.connect(emailData.getHost(), emailData.getEmail(), emailData.getPassword());
        //transport.connect();
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    @Override
    public void sendForgotPasswordEmail(String email, String username, int code) {

    }
}
