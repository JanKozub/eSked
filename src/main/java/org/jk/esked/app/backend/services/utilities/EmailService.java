package org.jk.esked.app.backend.services.utilities;

import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.jk.esked.app.backend.model.TokenValue;
import org.jk.esked.app.backend.model.entities.User;
import org.jk.esked.app.backend.model.types.EmailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

@Service
@PropertySource("/email.properties")
@PropertySource("/passwords.properties")
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        PROPERTIES.put("mail.smtp.auth", "true");
        PROPERTIES.put("mail.smtp.starttls.enable", "true");
    }

    private final String username;
    private final String password;
    private final String hostAddress;
    private final TokenService tokenService;

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

    public void sendEmail(User user, EmailType emailType) throws Exception {
        if (user == null) return;

        String subject = "", emailBody = "";
        UUID userId = user.getId();
        I18NProvider i18NProvider = VaadinService.getCurrent().getInstantiator().getI18NProvider();
        Locale locale = VaadinSession.getCurrent().getLocale();

        switch (emailType) {
            case NEWUSER -> {
                subject = i18NProvider.getTranslation("email.new.user.subject", locale);
                emailBody = i18NProvider.getTranslation("email.new.user.body", locale) + getUrl(userId, "verify", "verify");
            }
            case NEWPASSOWRD -> {
                subject = i18NProvider.getTranslation("email.new.password.subject", locale);
                emailBody = i18NProvider.getTranslation("email.new.password.body", locale) + getUrl(userId, user.getPassword(), "password");
            }
            case FORGOTPASS -> { //TODO password reset not working
                subject = i18NProvider.getTranslation("email.forgot.password.subject", locale);
                emailBody = i18NProvider.getTranslation("email.forgot.password.body", locale) + getUrl(userId, "forgot", "password");
            }
            case NEWUSERNAME -> {
                subject = i18NProvider.getTranslation("email.new.username.subject", locale);
                emailBody = i18NProvider.getTranslation("email.new.username.body", locale) + " \"" + user.getUsername() + "\".";
            }
            case NEWEMAIL -> {
                subject = i18NProvider.getTranslation("email.new.email.subject", locale);
                emailBody = i18NProvider.getTranslation("email.new.email.body", locale) + getUrl(userId, user.getEmail(), "email");
            }
        }

        sendEmailWithHTML(user.getEmail(), subject, emailBody);
    }

    private String getUrl(UUID userId, String value, String path) throws Exception {
        TokenValue tokenValue = new TokenValue();
        tokenValue.setUserId(userId);
        tokenValue.setValue(value);

        return " \n<a href=http://" + hostAddress + "/" + path + "/" + tokenService.encodeToken(tokenValue) + ">click</a>";
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
