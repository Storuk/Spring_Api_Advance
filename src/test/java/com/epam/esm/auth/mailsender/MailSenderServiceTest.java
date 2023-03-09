package com.epam.esm.auth.mailsender;

import com.epam.esm.enums.Role;
import com.epam.esm.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class MailSenderServiceTest {
    @InjectMocks
    private MailSenderService mailSenderService;
    @Mock
    private JavaMailSender mailSender;
    @Captor
    ArgumentCaptor<SimpleMailMessage> argumentCaptor;

    @Test
    void sendForgotPasswordVerificationCodeToEmail() {
        User user = User.builder()
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        mailSenderService.sendForgotPasswordVerificationCodeToEmail(user, "abc");
        verify(mailSender, times(1)).send(argumentCaptor.capture());
    }

    @Test
    void sendCreatedAccountMessage() {
        User user = User.builder()
                .firstName("Name")
                .lastName("LastName")
                .email("vladstoroschuk@gmail.com")
                .password("Password1234!")
                .role(Role.USER).build();
        mailSenderService.sendCreatedAccountMessage(user);
        verify(mailSender, times(1)).send(argumentCaptor.capture());
    }
}