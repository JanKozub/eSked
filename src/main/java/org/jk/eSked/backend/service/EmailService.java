package org.jk.eSked.backend.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.jk.eSked.backend.model.TokenValue;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;
import org.jk.eSked.backend.repositories.EmailDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

@Service
@PropertySource("email.properties")
@PropertySource("passwords.properties")
public class EmailService implements EmailDB { //TODO translate
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final String username;
    private final String password;
    private final String hostAddress;
    private final TokenService tokenService;

    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    public EmailService(@Value("${smtp.host}") String host, @Value("${smtp.port}") String port, @Value("${smtp.username}") String username,
                        @Value("${smtp.password}") String password, @Value("${host.address}") String hostAddress, TokenService tokenService) {
        PROPERTIES.put("mail.smtp.host", host);
        PROPERTIES.put("mail.smtp.port", port);
        this.username = username;
        this.password = password;
        this.hostAddress = hostAddress;
        this.tokenService = tokenService;

        log.info("Starting email service using user={}, host={}, port={}", username, host, port);
    }

    @Override
    public void sendEmail(User user, EmailType emailType) throws Exception {
        String subject = "";
        String emailBody = "";
        String url;
        TokenValue tokenValue = new TokenValue();
        switch (emailType) {
            case NEWUSER -> {
                subject = "Potwierdzenie rejstracji w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue("verify");
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + hostAddress + "/verify/" + url;

                log.warn(url);

                emailBody = "Witamy w eSked! Aktywuj konto klikając \n<a href=" + url + ">tutaj</a>";
            }
            case NEWPASSOWRD -> {
                subject = "Potwierdzenie zmiany hasła w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue(user.getPassword());
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + hostAddress + "/password/" + url;
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Aby zmienić hasło kliknij \n<a href=" + url + ">tutaj</a>";
            }
            case FORGOTPASS -> {
                subject = "Potwierdzenie zmiany hasła w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue("forgot");
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + hostAddress + "/password/" + url;
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Aby zmienić hasło kliknij \n<a href=" + url + ">tutaj</a>";
            }
            case NEWUSERNAME -> {
                subject = "Twoja nazwa użytkownika została zmieniona";
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Twoja nowa nazwa użytkownika to \"" + user.getUsername() + "\".";
            }
            case NEWEMAIL -> {
                subject = "Potwierdzenie zmiany email w eSked";
                tokenValue.setUserId(user.getId());
                tokenValue.setValue(user.getEmail());
                url = tokenService.encodeToken(tokenValue);
                url = "http://" + hostAddress + "/email/" + url;
                emailBody = "Dziękujemy za korzystanie z serwisu eSked. Aby zmienić email kliknij \n<a href=" + url + ">tutaj</a>";
            }
        }

        sendEmailWithHTML(user.getEmail(), subject, emailBody);
    }

    public void sendEmailWithHTML(String to, String subject, String message) {
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(PROPERTIES, authenticator);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            msg.setContent(message, "text/html");

            Transport.send(msg);
        } catch (MessagingException mex) {
            log.error("email service error: " + mex.getMessage());
        }
    }
}
