package com.epam.esm.auth;

import com.epam.esm.config.JwtService;
import com.epam.esm.enums.Role;
import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import com.epam.esm.verificationcode.VerificationCode;
import com.epam.esm.verificationcode.VerificationCodeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;
    private final VerificationCodeRepo verificationCodeRepo;

    public RegistrationAndAuthenticationResponse register(RegistrationRequest registrationRequest) {
        if (userRepo.findByEmail(registrationRequest.getEmail()).isEmpty()) {
            var user = User.builder()
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .role(Role.USER)
                    .build();
            userRepo.save(user);
            return RegistrationAndAuthenticationResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        }
        throw new InvalidDataException("User with such login already exists: " + registrationRequest.getEmail());
    }

    public RegistrationAndAuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var user = userRepo.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new ItemNotFoundException("User not exist"));
        return RegistrationAndAuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public RegistrationAndAuthenticationResponse refreshToken(String request) {
        User user = userRepo.findByEmail(jwtService.extractUserLogin(request))
                .orElseThrow(() -> new ItemNotFoundException("error occurred during the request"));

        if (jwtService.isTokenValid(request, user)) {

            return RegistrationAndAuthenticationResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();

        }
        throw new InvalidDataException("Invalid token");
    }

    public User resetPassword(ChangeUserPasswordRequest request) {
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

        return userRepo.save(user);
    }

    public boolean forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ItemNotFoundException("error occurred during the request"));
        VerificationCode verificationCode = VerificationCode.builder()
                .verificationCode(generateRandomCode())
                .user(user).build();
        verificationCodeRepo.save(verificationCode);
        sendForgotPasswordRandomStringToEmail(email, verificationCode.getVerificationCode());
        return true;
    }

    private void sendForgotPasswordRandomStringToEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vladstoroschuk1@gmail.com");
        message.setTo(email);
        message.setText(mailMessage(verificationCode));
        message.setSubject("Reset Password Code");
        mailSender.send(message);
    }

    private String mailMessage(String verificationCode) {
        return "Dear [[name]],\n"
                + "Here is code to reset and change your password:\n"
                + verificationCode
                + "\nCode expire in 60 minutes.\n"
                + "Thank you,\n"
                + "VladEntertainment.";
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