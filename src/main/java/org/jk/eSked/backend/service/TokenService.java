package org.jk.eSked.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jk.eSked.backend.model.TokenValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@PropertySource("email.properties")
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Cipher cipher;
    private final SecretKeySpec key;

    public TokenService(@Value("${json.key}") String encodeKey) throws Exception {
        byte[] keyData = encodeKey.getBytes(StandardCharsets.US_ASCII);
        key = new SecretKeySpec(keyData, "AES");

        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        log.info("Key for TokenService equals: {}", encodeKey);
    }

    String encodeToken(TokenValue tokenValue) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);

        String json = objectMapper.writeValueAsString(tokenValue);
        byte[] encryptedJson = cipher.doFinal(json.getBytes());
        String token = Base64.getEncoder().encodeToString(encryptedJson);
        token = URLEncoder.encode(token, StandardCharsets.UTF_8);
        token = token.replaceAll("%", "-");
        return token;
    }

    public TokenValue decodeToken(String url) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);

        url = url.replaceAll("-", "%");
        url = URLDecoder.decode(url, StandardCharsets.UTF_8);
        byte[] decodedTokenData = Base64.getDecoder().decode(url);
        byte[] decodedData = cipher.doFinal(decodedTokenData);

        return objectMapper.readValue(decodedData, TokenValue.class);
    }
}
