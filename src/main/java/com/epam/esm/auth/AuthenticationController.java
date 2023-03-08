package com.epam.esm.auth;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.auth.models.TokensResponse;
import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.utils.VerificationOfRequestsData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate-google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestParam String googleToken) {

        if (VerificationOfRequestsData.isStringValuesCorrect(googleToken)) {
            return ResponseEntity.ok(authenticationService.authenticateWithGoogle(googleToken));
        }
        throw new InvalidDataException("Invalid token format. Check your input");
    }

    @PostMapping("/register")
    public ResponseEntity<TokensResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        if (VerificationOfRequestsData.isRegistrationRequestCorrect(registrationRequest)) {
            return ResponseEntity.ok(authenticationService.register(registrationRequest));
        }
        throw new InvalidDataException("Invalid data. Check your inputs");
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        if (VerificationOfRequestsData.isAuthenticationRequestCorrect(authenticationRequest)) {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        }
        throw new InvalidDataException("Invalid email or password format. Check your inputs");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokensResponse> refreshToken(@RequestParam String token) {
        if (VerificationOfRequestsData.isStringValuesCorrect(token)) {
            return ResponseEntity.ok(authenticationService.refreshToken(token));
        }
        throw new InvalidDataException("Invalid token format");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (VerificationOfRequestsData.isEmailCorrect(email)) {
            authenticationService.forgotPassword(email);
            return ResponseEntity.ok(Map.of("message:",
                    "Verification code for resetting password sent to your email:" + email));
        }
        throw new InvalidDataException("Invalid email. Check your input");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ChangeUserPasswordRequest changeUserPasswordRequest) {
        if (VerificationOfRequestsData.isChangeUserPasswordRequestCorrect(changeUserPasswordRequest)) {
            authenticationService.resetPassword(changeUserPasswordRequest);
            return ResponseEntity.ok(Map.of("message:", "password successfully changed"));
        }
        throw new InvalidDataException("Invalid data. Check your inputs");
    }
}