package com.epam.esm.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAndAuthenticationResponse {
    private String accessToken;
    private String refreshToken;
}