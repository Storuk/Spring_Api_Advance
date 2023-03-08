package com.epam.esm.auth;

import com.epam.esm.auth.mailsender.MailSenderService;
import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.auth.models.TokensResponse;
import com.epam.esm.enums.Role;
import com.epam.esm.exceptions.InvalidDataException;
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void registerTest_WhenNewUser() {
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
    void registerTest_WhenUserWereAlreadyRegisteredWithEmail() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Name").lastName("LastName")
                .email("email@gmail.com").password("Password1234!")
                .repeatPassword("Password1234!").googleToken("token").build();
        User user = User.builder()
                .firstName("Name").lastName("LastName")
                .email("email@gmail.com").password(null)
                .role(Role.USER).build();
        User userForSave = User.builder().firstName("Name").lastName("LastName")
                .email("email@gmail.com").password("Password1234!")
                .role(Role.USER).build();
        User exctractedUser = User.builder().firstName("Name").lastName("LastName")
                .email("email@gmail.com").build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("token")
                .refreshToken("refreshToken")
                .build();
        when(userRepoMock.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.of(user));
        when(googleTokenServiceMock.isTokenValid(registrationRequest.getGoogleToken())).thenReturn(true);
        when(googleTokenServiceMock.extractUser(registrationRequest.getGoogleToken())).thenReturn(exctractedUser);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(registrationRequest.getPassword());
        when(userRepoMock.save(userForSave)).thenReturn(userForSave);
        when(jwtServiceMock.generateToken(user)).thenReturn(tokensResponse.getAccessToken());
        when(jwtServiceMock.generateRefreshToken(user)).thenReturn(tokensResponse.getRefreshToken());
        assertEquals(tokensResponse, authenticationServiceMock.register(registrationRequest));
    }

    @Test
    void registerTest_ThrowInvalidDataException_WhenUserWereAlreadyExist() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Name").lastName("LastName")
                .email("email@gmail.com").password("Password1234!")
                .repeatPassword("Password1234!").googleToken("token").build();
        User user = User.builder()
                .firstName("Name").lastName("LastName")
                .email("email@gmail.com").password("Password1234!")
                .role(Role.USER).build();
        when(userRepoMock.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.of(user));
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> authenticationServiceMock.register(registrationRequest));
        assertEquals("User with such email already exists: " + registrationRequest.getEmail(), message.getMessage());
    }

    @Test
    void registerTest_ThrowAccessDeniedException_WhenProblemInTokenStructure() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Name").lastName("LastName")
                .email("email@gmail.com").password("Password1234!")
                .repeatPassword("Password1234!").googleToken("token").build();
        User user = User.builder()
                .firstName("Name").lastName("LastName")
                .email("email@gmail.com").password(null)
                .role(Role.USER).build();
        User exctractedUser = User.builder().firstName("Name").lastName("LastName")
                .email("anotherEmail@gmail.com").build();
        when(userRepoMock.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.of(user));
        when(googleTokenServiceMock.isTokenValid(registrationRequest.getGoogleToken())).thenReturn(true);
        when(googleTokenServiceMock.extractUser(registrationRequest.getGoogleToken())).thenReturn(exctractedUser);
        AccessDeniedException message = assertThrows(AccessDeniedException.class,
                () -> authenticationServiceMock.register(registrationRequest));
        assertEquals("There is problem in token structure", message.getMessage());
    }

    @Test
    void resetPasswordTest() {
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
    void resetPasswordTest_ThrowInvalidDataException_WhenInvalidToken() {
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .repeatPassword("Password1234!")
                .verificationCode("cod")
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
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> authenticationServiceMock.resetPassword(changeUserPasswordRequest));
        assertEquals("Invalid verificationCode.", message.getMessage());
    }

    @Test
    void resetPasswordTest_ThrowInvalidDataException_WhenVerificationCodeExpired() {
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
                .createDate(Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond() - 10000 * 60 * 60)))
                .build();
        when(userRepoMock.findByEmail(changeUserPasswordRequest.getEmail())).thenReturn(Optional.of(user));
        when(verificationCodeRepo.findByUserId(user.getId())).thenReturn(Optional.of(verificationCode));
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> authenticationServiceMock.resetPassword(changeUserPasswordRequest));
        assertEquals("Verification code is expired.", message.getMessage());
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
    void authenticateTest() {
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
    void refreshTokenTest() {
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
    void refreshTokenTest_ThrowInvalidDataException_WhenInvalidToken() {
        String token = "token";
        User user = User.builder()
                .id(1L)
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        when(jwtServiceMock.extractUserEmail(token)).thenReturn(user.getEmail());
        when(userRepoMock.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtServiceMock.isTokenValid(token, user)).thenReturn(false);
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> authenticationServiceMock.refreshToken(token));
        assertEquals("Invalid token", message.getMessage());
    }

    @Test
    void authenticateWithGoogleTest() {
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

    @Test
    void authenticateWithGoogleTest_ThrowInvalidDataException_WhenInvalidGoogleToken() {
        String token = "googleToken";
        when(googleTokenServiceMock.isTokenValid(token)).thenReturn(false);
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> authenticationServiceMock.authenticateWithGoogle(token));
        assertEquals("Invalid token", message.getMessage());
    }

    @Test
    void authenticateWithGoogleTest_ThrowInvalidDataException_WhenProblemInTokenStructure() {
        String token = "googleToken";
        when(googleTokenServiceMock.isTokenValid(token)).thenReturn(true);
        when(googleTokenServiceMock.extractUser(token)).thenReturn(new User());
        InvalidDataException message = assertThrows(InvalidDataException.class,
                () -> authenticationServiceMock.authenticateWithGoogle(token));
        assertEquals("There is problem in token structure", message.getMessage());
    }
}