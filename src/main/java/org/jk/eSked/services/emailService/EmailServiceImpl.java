package org.jk.eSked.services.emailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Service
@PropertySource("email.properties")
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static Session getMailSession;
    private static Transport transport;
    private static Properties mailServerProperties;
    private String host;
    private String user;
    private String password;

    public EmailServiceImpl(@Value("${smtp.host}") String host, @Value("${smtp.port}") String port, @Value("${smtp.user}") String user, @Value("${smtp.password}") String password) throws IOException {
        this.host = host;
        this.user = user;
        this.password = password;
        log.info("Starting email service using {}@{}:{}", user, host, port);
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", port);
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
    }

    @Override
    public void sendEmail(String email, String subject, String emailBody) throws MessagingException {
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        transport = getMailSession.getTransport("smtp");
        transport.connect(host, user, password);

        generateAndSendMessage(email, subject, emailBody);

        transport.close();
    }

    private void generateAndSendMessage(String email, String subject, String body) throws MessagingException {
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(body, "text/html; charset=ISO-8859-2");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    }
}
