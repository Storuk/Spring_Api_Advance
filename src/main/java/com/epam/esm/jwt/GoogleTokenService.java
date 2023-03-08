package com.epam.esm.jwt;

import com.epam.esm.jwt.FeignClient.FeignClient;
import com.epam.esm.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleTokenService {
    private final FeignClient feignClient;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String aud;

    public GoogleTokenService(FeignClient feignClient) {
        this.feignClient = feignClient;
    }

    private String extractEmail(String token) {
        return feignClient.verifyTokenAndGetMapOfClaims(token).get("email");
    }

    private String extractFirstName(String token) {
        return feignClient.verifyTokenAndGetMapOfClaims(token).get("given_name");
    }

    private String extractLastName(String token) {
        return feignClient.verifyTokenAndGetMapOfClaims(token).get("family_name");
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
        return feignClient.verifyTokenAndGetMapOfClaims(token).get("aud").equals(aud);
    }
}