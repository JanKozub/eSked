package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.EmailType;
import org.jk.eSked.backend.model.User;

public interface EmailDB {

    void sendEmail(User user, EmailType emailType) throws Exception;

}
