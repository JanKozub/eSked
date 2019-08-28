package org.jk.eSked.services.emailService.DB;

import org.jk.eSked.dao.EmailDao;
import org.jk.eSked.services.emailService.EmailData;

public class DBEmailService implements DBEmailInterface {
    private EmailDao emailDao;

    public DBEmailService(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    @Override
    public EmailData getData() {
        return emailDao.getEmailData();
    }

    @Override
    public void setData(EmailData emailData) {
        emailDao.setEmailData(emailData);
    }
}
