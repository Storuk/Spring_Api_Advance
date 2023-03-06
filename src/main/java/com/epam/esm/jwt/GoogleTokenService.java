package com.epam.esm.jwt;

import com.epam.esm.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class GoogleTokenService {
    private Map<String, String> getMapOfClaimsFromToken(String token) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        try {
            return new ObjectMapper().readValue(payload, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    public String extractEmail(String token) {
        return getMapOfClaimsFromToken(token).get("email");
    }

    private String extractFirstName(String token) {
        return getMapOfClaimsFromToken(token).get("given_name");
    }

    private String extractLastName(String token) {
        return getMapOfClaimsFromToken(token).get("family_name");
    }

    public User extractUser(String token) {
        return User.builder()
                .email(extractEmail(token))
                .firstName(extractFirstName(token))
                .lastName(extractLastName(token)).build();
    }

    public boolean isTokenValid(String token) {
        return isTokenExpired(token);
    }

    private Long extractExpiration(String token) {
        return Long.parseLong(getMapOfClaimsFromToken(token).get("exp"));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token) + 3 * 3600 >= Instant.now().getEpochSecond();
    }
}