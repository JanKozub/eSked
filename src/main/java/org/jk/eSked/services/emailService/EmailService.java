package org.jk.eSked.services.emailService;

import javax.mail.MessagingException;

public interface EmailService {

    void sendEmail(String email, String subject, String emailBody) throws MessagingException;

}
