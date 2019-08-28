package org.jk.eSked.services.emailService.DB;

import org.jk.eSked.services.emailService.EmailData;

public interface DBEmailInterface {
    EmailData getData();

    void setData(EmailData emailData);
}
