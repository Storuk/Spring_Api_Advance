package com.epam.esm.jwt;

import com.epam.esm.jwt.FeignClient.FeignClient;
import com.epam.esm.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class GoogleTokenServiceTest {
    @InjectMocks
    private GoogleTokenService googleJwtService;
    @Mock
    private FeignClient apiClient;

    @Test
    void extractUser() {
        User user = User.builder()
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .build();
        Map<String, String> propTest = Map.of("email", "vladstoroschuk@gmail.com",
                "given_name", "Name",
                "family_name", "LastName");
        when(apiClient.verifyTokenAndGetMapOfClaims("token")).thenReturn(propTest);
        assertEquals(user, googleJwtService.extractUser("token"));
    }

    @Test
    void isTokenValid() {
        Map<String, String> propTest = Map.of("email", "", "aud", "aud");
        when(apiClient.verifyTokenAndGetMapOfClaims("token")).thenReturn(propTest);
        assertFalse(googleJwtService.isTokenValid("token"));
    }
}