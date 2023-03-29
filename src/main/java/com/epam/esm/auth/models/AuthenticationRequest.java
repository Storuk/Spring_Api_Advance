package com.epam.esm.auth.models;

import com.epam.esm.ExcludeCoverage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ExcludeCoverage
public class AuthenticationRequest {
    private String email;
    private String password;
}