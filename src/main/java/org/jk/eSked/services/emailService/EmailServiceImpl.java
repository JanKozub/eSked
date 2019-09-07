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
    private static Properties properties;
    private static Properties mailServerProperties;

    public EmailServiceImpl() throws IOException {
        URL resource = EmailServiceImpl.class.getClassLoader().getResource("email.properties");
        log.info("Loading resource from {}", resource);

        assert resource != null;
        try (InputStream stream = resource.openStream()) {
            properties = new Properties();
            properties.load(stream);

            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", properties.getProperty("port"));
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
        }
    }

    @Override
    public void sendEmail(String email, String subject, String emailBody) throws MessagingException {
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        transport = getMailSession.getTransport("smtp");
        transport.connect(properties.getProperty("host"), properties.getProperty("user"), properties.getProperty("password"));

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
