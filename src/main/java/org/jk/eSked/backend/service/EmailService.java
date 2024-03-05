package org.jk.eSked.backend.service;

import org.jk.eSked.backend.model.TokenValue;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.repositories.EmailDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@PropertySource("email.properties")
public class EmailService implements EmailDB {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private static Session getMailSession;
    private static Transport transport;
    private static Properties mailServerProperties;
    private final String host;
    private final String emailUser;
    private final String password;
    private final String serverAddress;
    private final TokenService tokenService;

    public EmailService(@Value("${smtp.host}") String host, @Value("${smtp.port}") String port, @Value("${smtp.user}") String emailUser,
                        @Value("${smtp.password}") String password, @Value("${host.address}") String serverAddress, TokenService tokenService) {
        this.host = host;
        this.emailUser = emailUser;
        this.password = password;
        this.serverAddress = serverAddress;
        this.tokenService = tokenService;

        log.info("Starting email service using {}@{}:{}", emailUser, host, port);
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", port);
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
    }

    @Override
    public void sendEmail(User user, EmailType emailType) throws Exception {
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        transport = getMailSession.getTransport("smtp");
        transport.connect(host, emailUser, password);

        String subject = "";
        String emailBody = "";
        String url;
        TokenValue tokenValue = new TokenValue();
        switch (emailType) {
            case NEWUSER:
                subject = "Potwierdzenie rejstracji w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue("verify");
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + serverAddress + "/verify/" + url;

                log.warn(url);

                emailBody = "Witamy w eSked! Aktywuj konto klikając \n<a href=" + url + ">tutaj</a>";
                break;
            case NEWPASSOWRD:
                subject = "Potwierdzenie zmiany hasła w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue(user.getPassword());
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + serverAddress + "/password/" + url;
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Aby zmienić hasło kliknij \n<a href=" + url + ">tutaj</a>";
                break;
            case FORGOTPASS:
                subject = "Potwierdzenie zmiany hasła w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue("forgot");
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + serverAddress + "/password/" + url;
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Aby zmienić hasło kliknij \n<a href=" + url + ">tutaj</a>";
                break;
            case NEWUSERNAME:
                subject = "Twoja nazwa użytkownika została zmieniona";
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Twoja nowa nazwa użytkownika to \"" + user.getUsername() + "\".";
                break;
            case NEWEMAIL:
                subject = "Potwierdzenie zmiany email w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue(user.getEmail());
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + serverAddress + "/email/" + url;
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Aby zmienić email kliknij \n<a href=" + url + ">tutaj</a>";
                break;
        }
        generateAndSendMessage(user.getEmail(), subject, emailBody);

        transport.close();
    }

    private void generateAndSendMessage(String email, String subject, String body) throws Exception {
        MimeMessage generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(body, "text/html; charset=ISO-8859-2");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    }
}
