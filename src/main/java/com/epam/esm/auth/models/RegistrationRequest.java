package com.epam.esm.auth.models;

import com.epam.esm.anotations.ExcludeModelsCoverage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcludeModelsCoverage
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String repeatPassword;
    private String googleToken;
}