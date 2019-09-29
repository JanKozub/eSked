package org.jk.eSked.backend.service;


import org.jk.eSked.backend.model.TokenValue;
import org.junit.Test;

import java.util.UUID;

public class TokenServiceTest {

    @Test
    public void generateURL() throws Exception {
        //GIVEN
        TokenValue tokenValue = new TokenValue();
        tokenValue.setUserId(UUID.randomUUID());
        tokenValue.setValue("verify");
        //String url = TokenService.encodeToken(tokenValue);
        //WHEN
        //TokenValue newTokenValue = TokenService.decodeToken(url);
        //System.out.println(newTokenValue.getUserId());
        //THEN
        //Assert.assertTrue(newTokenValue.getUserId().equals(tokenValue.getUserId()));
    }

    @Test
    public void decodeToken() {
    }
}