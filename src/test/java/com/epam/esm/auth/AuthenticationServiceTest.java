package com.epam.esm.auth;

import com.epam.esm.auth.mailsender.MailSenderService;
import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.auth.models.TokensResponse;
import com.epam.esm.enums.Role;
import com.epam.esm.jwt.GoogleTokenService;
import com.epam.esm.jwt.JwtService;
import com.epam.esm.user.User;
import com.epam.esm.user.UserRepo;
import com.epam.esm.verificationcode.VerificationCode;
import com.epam.esm.verificationcode.VerificationCodeRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationServiceMock;
    @Mock
    private UserRepo userRepoMock;
    @Mock
    private JwtService jwtServiceMock;
    @Mock
    private GoogleTokenService googleTokenServiceMock;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private VerificationCodeRepo verificationCodeRepo;
    @Mock
    private MailSenderService mailSenderService;

    @Test
    void register() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .repeatPassword("Password1234!")
                .build();
        User user = User.builder()
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("token")
                .refreshToken("refreshToken")
                .build();
        when(userRepoMock.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(registrationRequest.getPassword());
        when(userRepoMock.save(user)).thenReturn(user);
        when(jwtServiceMock.generateToken(user)).thenReturn(tokensResponse.getAccessToken());
        when(jwtServiceMock.generateRefreshToken(user)).thenReturn(tokensResponse.getRefreshToken());
        assertEquals(tokensResponse, authenticationServiceMock.register(registrationRequest));
    }

    @Test
    void resetPassword() {
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .repeatPassword("Password1234!")
                .verificationCode("code")
                .build();
        User user = User.builder()
                .id(1L)
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        VerificationCode verificationCode = VerificationCode.builder()
                .id(1L)
                .verificationCode("code")
                .user(user)
                .createDate(Date.from(Instant.now()))
                .build();
        when(userRepoMock.findByEmail(changeUserPasswordRequest.getEmail())).thenReturn(Optional.of(user));
        when(verificationCodeRepo.findByUserId(user.getId())).thenReturn(Optional.of(verificationCode));
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(userRepoMock.save(user)).thenReturn(user);
        assertTrue(authenticationServiceMock.resetPassword(changeUserPasswordRequest));
    }

    @Test
    void forgotPasswordTest_ThrowAccessDeniedException() {
        User user = User.builder()
                .id(1L)
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password(null)
                .role(Role.USER).build();
        when(userRepoMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        AccessDeniedException message = assertThrows(AccessDeniedException.class,
                () -> authenticationServiceMock.forgotPassword(user.getEmail()));
        assertEquals("You were registered with google, so you can`t reset your password", message.getMessage());
    }

    @Test
    void authenticate() {
        User user = User.builder()
                .id(1L)
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("vladstoroschuk1@gmail.com")
                .password("Password1234!")
                .build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("token")
                .refreshToken("refreshToken")
                .build();
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ))).thenReturn(null);
        when(userRepoMock.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtServiceMock.generateToken(user)).thenReturn(tokensResponse.getAccessToken());
        when(jwtServiceMock.generateRefreshToken(user)).thenReturn(tokensResponse.getRefreshToken());
        assertEquals(tokensResponse, authenticationServiceMock.authenticate(authenticationRequest));
    }

    @Test
    void refreshToken() {
        String token = "token";
        User user = User.builder()
                .id(1L)
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("token")
                .refreshToken("refreshToken")
                .build();
        when(jwtServiceMock.extractUserEmail(token)).thenReturn(user.getEmail());
        when(userRepoMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtServiceMock.isTokenValid(token, user)).thenReturn(true);
        when(jwtServiceMock.generateToken(user)).thenReturn(tokensResponse.getAccessToken());
        when(jwtServiceMock.generateRefreshToken(user)).thenReturn(tokensResponse.getRefreshToken());
        assertEquals(tokensResponse, authenticationServiceMock.refreshToken(token));
    }

    @Test
    void authenticateWithGoogle() {
        String token = "googleToken";
        User user = User.builder()
                .id(1L)
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("token")
                .refreshToken("refreshToken")
                .build();
        when(googleTokenServiceMock.isTokenValid(token)).thenReturn(true);
        when(googleTokenServiceMock.extractUser(token)).thenReturn(user);
        when(userRepoMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtServiceMock.generateToken(user)).thenReturn(tokensResponse.getAccessToken());
        when(jwtServiceMock.generateRefreshToken(user)).thenReturn(tokensResponse.getRefreshToken());
        assertEquals(tokensResponse, authenticationServiceMock.authenticateWithGoogle(token));
    }
}