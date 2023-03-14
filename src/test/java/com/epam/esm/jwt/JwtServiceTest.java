package com.epam.esm.jwt;

import com.epam.esm.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService = new JwtService("4528482B4D6251655368566D597133743677397A24432646294A404E63526655");
    private String token;
    private final User user = User.builder()
            .firstName("Name")
            .lastName("LastName")
            .email("email")
            .build();

    @BeforeEach
    public void setup() {
        token = jwtService.generateToken(user);
    }

    @Test
    void extractUserEmail() {
        assertEquals(jwtService.extractUserEmail(token), "email");
    }

    @Test
    void generateToken() {
        assertNotNull(jwtService.generateToken(user));
    }

    @Test
    void generateRefreshToken() {
        assertNotNull(jwtService.generateRefreshToken(user));
    }

    @Test
    void isTokenValid() {
        assertTrue(jwtService.isTokenValid(token, user));
    }
}