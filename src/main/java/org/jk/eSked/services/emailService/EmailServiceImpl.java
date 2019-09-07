package org.jk.eSked.services.emailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static Session getMailSession;
    private static Transport transport;

    public EmailServiceImpl() throws MessagingException, IOException {
        URL resource = EmailServiceImpl.class.getClassLoader().getResource("email.properties");
        log.info("Loading resource from {}", resource);

        try (InputStream stream = resource.openStream()) {
            Properties p = new Properties();
            p.load(stream);

            Properties mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", p.getProperty("port"));
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            transport = getMailSession.getTransport("smtp");
            transport.connect(p.getProperty("host"), p.getProperty("user"), p.getProperty("password"));
        }
    }

    @Override
    public void sendEmail(String email, String subject, String emailBody) throws MessagingException {
        generateAndSendMessage(email, subject, emailBody);
    }

    private void generateAndSendMessage(String email, String subject, String body) throws MessagingException {
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(body, "text/html; charset=ISO-8859-2");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    }
}
