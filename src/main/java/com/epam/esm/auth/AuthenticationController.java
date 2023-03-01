package com.epam.esm.auth;

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

    @PostMapping("/register")
    public ResponseEntity<RegistrationAndAuthenticationResponse> register(@RequestBody RegistrationRequest registrationRequest) {
        if (VerificationOfRequestsData.isRegistrationRequestCorrect(registrationRequest)) {
            return ResponseEntity.ok(authenticationService.register(registrationRequest));
        }
        throw new InvalidDataException("Invalid data. Check your inputs");
    }

    @PostMapping("/login")
    public ResponseEntity<RegistrationAndAuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        if (VerificationOfRequestsData.isAuthenticationRequestCorrect(authenticationRequest)) {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        }
        throw new InvalidDataException("Invalid email or password format. Check your inputs");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RegistrationAndAuthenticationResponse> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (VerificationOfRequestsData.isEmailCorrect(email)) {
            authenticationService.forgotPassword(email);
            return ResponseEntity.ok("ok");
        }
        throw new InvalidDataException("Invalid email. Check your input");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ChangeUserPasswordRequest changeUserPasswordRequest) {
        if(VerificationOfRequestsData.isChangeUserPasswordRequestCorrect(changeUserPasswordRequest)) {
            return ResponseEntity.ok(Map.of("Success",
                    authenticationService.resetPassword(changeUserPasswordRequest)));
        }
        throw new InvalidDataException("Invalid data. Check your inputs");
    }
}