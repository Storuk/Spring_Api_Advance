package com.epam.esm.auth;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.ChangeUserPasswordRequest;
import com.epam.esm.auth.models.RegistrationRequest;
import com.epam.esm.auth.models.TokensResponse;
import com.epam.esm.exceptions.controlleradvice.ControllerAdvisor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mvc;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());

        mvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
    }

    @Test
    @SneakyThrows
    void authenticateWithGoogleTest() {
        String googleToken = "googleToken";
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();
        when(authenticationService.authenticateWithGoogle(googleToken)).thenReturn(tokensResponse);
        MockHttpServletResponse response = mvc.perform(post("/authentication/authenticate-google?googleToken=googleToken").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void authenticateWithGoogleTest_ThrowInvalidDataException_WhenGoogleTokenIsEmpty() {
        MockHttpServletResponse response = mvc.perform(post("/authentication/authenticate-google?googleToken=").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void registerTest() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk1@gmail.com")
                .password("Password1234!")
                .repeatPassword("Password1234!")
                .build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();
        when(authenticationService.register(registrationRequest)).thenReturn(tokensResponse);
        MockHttpServletResponse response = mvc.perform(post("/authentication/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void registerTest_ThrowInvalidDataException_WhenInvalidRegisterRequest() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder().build();
        MockHttpServletResponse response = mvc.perform(post("/authentication/register").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void authenticateTest() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("vladstoroschuk1@gmail.com")
                .password("Password1234!")
                .build();
        TokensResponse tokensResponse = TokensResponse.builder()
                .accessToken("access")
                .refreshToken("refresh")
                .build();
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(tokensResponse);
        MockHttpServletResponse response = mvc.perform(post("/authentication/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void authenticateTest_ThrowInvalidDataException_WhenInvalidAuthenticationRequest() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().build();
        MockHttpServletResponse response = mvc.perform(post("/authentication/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authenticationRequest)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void refreshTokenTest() {
        String refreshToken = "refreshToken";
        TokensResponse tokensResponse = TokensResponse.builder().accessToken("access").refreshToken("refresh").build();
        when(authenticationService.refreshToken(refreshToken)).thenReturn(tokensResponse);
        MockHttpServletResponse response = mvc.perform(post("/authentication/refresh-token?token=refreshToken").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void refreshTokenTest_ThrowInvalidDataException_WhenInvalidRefreshTokenFormat() {
        MockHttpServletResponse response = mvc.perform(post("/authentication/refresh-token?token=").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void forgotPasswordTest() {
        String email = "vladstoroschuk1@gmail.com";
        when(authenticationService.forgotPassword(email)).thenReturn(true);
        MockHttpServletResponse response = mvc.perform(post("/authentication/forgot-password?email=vladstoroschuk1@gmail.com").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void forgotPasswordTest_ThrowInvalidDataException_WhenInvalidEmailFormat() {
        MockHttpServletResponse response = mvc.perform(post("/authentication/forgot-password?email=").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void resetPasswordTest() {
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .email("vladstoroschuk1@gmail.com")
                .password("Password1234!")
                .repeatPassword("Password1234!")
                .verificationCode("code")
                .build();
        when(authenticationService.resetPassword(changeUserPasswordRequest)).thenReturn(true);
        MockHttpServletResponse response = mvc.perform(post("/authentication/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeUserPasswordRequest)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @SneakyThrows
    void resetPasswordTest_ThrowInvalidDataException_WhenInvalidChangeUserPasswordRequest() {
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder().build();
        MockHttpServletResponse response = mvc.perform(post("/authentication/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeUserPasswordRequest)).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}