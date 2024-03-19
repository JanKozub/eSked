package org.jk.esked.app.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@PropertySource("/passwords.properties")
public class EncryptionService {
    private final String bCryptKey;

    public EncryptionService(@Value("${json.key2}") String bCryptKey) {
        this.bCryptKey = bCryptKey;
    }

    public String encodePassword(String password) {
        return BCrypt.hashpw(password, bCryptKey);
    }
}
