package org.jk.eSked.services.emailService;

import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    static Transport transport;

    public EmailServiceImpl() throws MessagingException, IOException {
        FileReader reader = new FileReader("C:\\Users\\Jan\\Desktop\\Java\\esked\\src\\main\\resources\\email.properties");

        Properties p = new Properties();
        p.load(reader);

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", p.getProperty("port"));
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        transport = getMailSession.getTransport("smtp");
        transport.connect(p.getProperty("host"), p.getProperty("user"), p.getProperty("password"));
    }

    @Override
    public void sendNewUserEmail(String email, String username, int code) throws MessagingException {
        String emailBody = "Witaj " + username + "," +
                "<br><br>Dziękujemy za zarejstrowanie się na naszej stronie, oto twój kod weryfikacji: " +
                "<br><br>" + code + "<br><br> Z poważaniem, <br>Zespół eSked";

        generateAndSendMessage(email, "Potwierdź rejstrację w eSked!", emailBody);
    }

    @Override
    public void sendForgotPasswordEmail(String email, String username, int code) throws MessagingException {
        String emailBody = "Witaj " + username + "," + "<br><br>Twój kod zmiany hasła to: " + "<br><br>" + code +
                "<br><br>" + "Teraz możesz wpisać go na stronie!" + "<br><br> Z poważaniem, <br>Zespół eSked";
        generateAndSendMessage(email, "Prośba zmiany hasła w eSked!", emailBody);
    }

    private void generateAndSendMessage(String email, String subject, String body) throws MessagingException {
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        generateMailMessage.setSubject(subject);
        generateMailMessage.setContent(body, "text/html; charset=ISO-8859-2");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
    }
}
