package com.epam.esm.auth;

import com.epam.esm.auth.mailsender.MailSenderService;
import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.auth.models.TokensResponse;
import com.epam.esm.enums.Role;
import com.epam.esm.exceptions.InvalidDataException;
import com.epam.esm.exceptions.ItemNotFoundException;
import com.epam.esm.exceptions.NoAccessException;
import com.epam.esm.exceptions.UserNotFoundException;
import com.epam.esm.jwt.GoogleTokenService;
import com.epam.esm.jwt.JwtService;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import com.epam.esm.verificationcode.VerificationCode;
import com.epam.esm.verificationcode.VerificationCodeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
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
    private final GoogleTokenService googleTokenService;

    public TokensResponse register(RegistrationRequest registrationRequest) {
        Optional<User> userByEmail = userRepo.findByEmail(registrationRequest.getEmail());
        User user = new User();
        if (userByEmail.isEmpty()) {
            user = User.builder()
                    .email(registrationRequest.getEmail())
                    .password(passwordEncoder.encode(registrationRequest.getPassword()))
                    .firstName(registrationRequest.getFirstName())
                    .lastName(registrationRequest.getLastName())
                    .role(Role.USER)
                    .build();
        } else if (wereUserRegisteredWithGoogleAccount(userByEmail, registrationRequest)) {
            user = userByEmail.get();
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        }
        mailSenderService.sendCreatedAccountMessage(user);
        userRepo.save(user);
        return TokensResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public boolean forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with such email is not exists"));
        if (user.getPassword() != null) {
            VerificationCode verificationCode = VerificationCode.builder()
                    .verificationCode(generateRandomCode())
                    .user(user).build();
            verificationCodeRepo.save(verificationCode);
            mailSenderService.sendForgotPasswordVerificationCodeToEmail(user, verificationCode.getVerificationCode());
            return true;
        }
        throw new NoAccessException("You were Authenticated with google, so you can`t reset your password");
    }

    public boolean resetPassword(ChangeUserPasswordRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("There are no user with such email"));
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

    public TokensResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepo.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid User Credentials"));
        return TokensResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public TokensResponse refreshToken(String token) {
        User user = userRepo.findByEmail(jwtService.extractUserEmail(token))
                .orElseThrow(() -> new UserNotFoundException("Invalid token"));

        if (jwtService.isTokenValid(token, user)) {
            return TokensResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        }
        throw new InvalidDataException("Invalid token");
    }

    public TokensResponse authenticateWithGoogle(String googleToken) {
        if (googleTokenService.isTokenValid(googleToken)) {
            User extractedUser = googleTokenService.extractUser(googleToken);
            if (extractedUser.getEmail() != null) {
                Optional<User> user = userRepo.findByEmail(extractedUser.getEmail())
                        .or(() -> Optional.of(userRepo.save(User.builder().email(extractedUser.getEmail().trim())
                                .firstName(extractedUser.getFirstName().trim()).lastName(extractedUser.getLastName().trim())
                                .role(Role.USER).build())));

                return TokensResponse.builder()
                        .accessToken(jwtService.generateToken(user.get()))
                        .refreshToken(jwtService.generateRefreshToken(user.get()))
                        .build();
            }
            throw new InvalidDataException("There is problem in token signature");
        }
        throw new InvalidDataException("Invalid token");
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

    private boolean wereUserRegisteredWithGoogleAccount(Optional<User> userByEmail, RegistrationRequest registrationRequest){
        if (userByEmail.isPresent()) {
            if (userByEmail.get().getPassword() == null) {
                if (registrationRequest.getGoogleToken() != null
                        && googleTokenService.isTokenValid(registrationRequest.getGoogleToken())
                        && googleTokenService.extractEmail(registrationRequest.getGoogleToken()).equals(userByEmail.get().getEmail())) {
                    return true;
                }
                throw new AccessDeniedException("There is problem in token signature");
            }
            throw new InvalidDataException("User with such email already exists: " + registrationRequest.getEmail());
        }
        return false;
    }
}