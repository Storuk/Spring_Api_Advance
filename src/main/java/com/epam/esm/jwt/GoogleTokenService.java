package com.epam.esm.jwt;

import com.epam.esm.jwt.FeignClient.FeignClient;
import com.epam.esm.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleTokenService {
    private final FeignClient feignClient;
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "given_name";
    private static final String LAST_NAME = "family_name";
    private static final String AUD = "aud";
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String aud;

    public GoogleTokenService(FeignClient feignClient) {
        this.feignClient = feignClient;
    }

    private String extractEmail(String token) {
        return feignClient.verifyTokenAndGetMapOfClaims(token).get(EMAIL);
    }

    private String extractFirstName(String token) {
        return feignClient.verifyTokenAndGetMapOfClaims(token).get(FIRST_NAME);
    }

    private String extractLastName(String token) {
        return feignClient.verifyTokenAndGetMapOfClaims(token).get(LAST_NAME);
    }

    public User extractUser(String token) {
        return User.builder()
                .email(extractEmail(token))
                .firstName(extractFirstName(token))
                .lastName(extractLastName(token)).build();
    }

    public boolean isTokenValid(String token) {
        return isValidTokenAud(token);
    }

    private boolean isValidTokenAud(String token) {
        System.out.println(aud);
        return feignClient.verifyTokenAndGetMapOfClaims(token).get(AUD).equals(aud);
    }
}