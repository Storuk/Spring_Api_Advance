package com.epam.esm.auth.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserPasswordRequest {
    private String email;
    private String password;
    private String repeatPassword;
    private String verificationCode;
}
