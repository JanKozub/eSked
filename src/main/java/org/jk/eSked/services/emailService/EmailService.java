package org.jk.eSked.services.emailService;

import javax.mail.MessagingException;

public interface EmailService {

    void sendNewUserEmail(String email, String username, int code) throws MessagingException;

    void sendForgotPasswordEmail(String email, String username, int code);
}
