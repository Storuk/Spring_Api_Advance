package com.epam.esm.auth;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.auth.models.TokensResponse;
import com.epam.esm.config.JwtService;
import com.epam.esm.enums.Role;
import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.mailsender.MailSenderService;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import com.epam.esm.verificationcode.VerificationCode;
import com.epam.esm.verificationcode.VerificationCodeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailSenderService mailSenderService;
    private final VerificationCodeRepo verificationCodeRepo;

    public TokensResponse register(RegistrationRequest registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()).isEmpty()) {
            var user = User.builder()
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .role(Role.USER)
                    .build();
            userRepo.save(user);
            return TokensResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        }
        throw new InvalidDataException("User with such login already exists: " + registrationRequest.getEmail());
    }

    public TokensResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepo.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ItemNotFoundException("User not exist"));
        return TokensResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public TokensResponse refreshToken(String request) {
        User user = userRepo.findByEmail(jwtService.extractUserEmail(request))
                .orElseThrow(() -> new ItemNotFoundException("There are no user with such email or invalid token"));

        if (jwtService.isTokenValid(request, user)) {

            return TokensResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();

        }
        throw new InvalidDataException("Invalid token");
    }

    public boolean resetPassword(ChangeUserPasswordRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ItemNotFoundException("User with such email is not exists"));
        VerificationCode verificationCode = verificationCodeRepo.findByUserId(user.getId())
                .orElseThrow(() -> new ItemNotFoundException("This user were not trying to change password"));

        if (!request.getVerificationCode().equals(verificationCode.getVerificationCode())) {
            throw new InvalidDataException("Invalid verificationCode.");
        } else if (!isVerificationCodeExpired(verificationCode)) {
            throw new InvalidDataException("Verification code is expired.");
        }

        verificationCodeRepo.deleteById(verificationCode.getId());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
        return true;
    }

    public boolean forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException("User with such email is not exists"));
        VerificationCode verificationCode = VerificationCode.builder()
                .verificationCode(generateRandomCode())
                .user(user).build();
        verificationCodeRepo.save(verificationCode);
        mailSenderService.sendForgotPasswordRandomStringToEmail(user, verificationCode.getVerificationCode());
        return true;
    }

    private String generateRandomCode() {
        Random random = new Random();
        return random.ints(48, 123)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(50)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private boolean isVerificationCodeExpired(VerificationCode verificationCode) {
        return new Date().before(new Date(verificationCode.getCreateDate().getTime() + 1000 * 60 * 60));
    }
}