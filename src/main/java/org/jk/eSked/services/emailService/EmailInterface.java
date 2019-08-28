package org.jk.eSked.services.emailService;

import org.jk.eSked.services.emailService.DB.DBEmailInterface;

import javax.mail.MessagingException;

public interface EmailInterface {

    void sendNewUserEmail(String email, String username, DBEmailInterface dbEmailInterface) throws MessagingException;

    void sendForgotPasswordEmail(String email, String username, int code);
}
