package org.jk.eSked.backend.repositories;

import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.types.EmailType;

public interface EmailDB {

    void sendEmail(User user, EmailType emailType) throws Exception;

}
