package com.epam.esm.mailsender;

import com.epam.esm.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;

    public void sendForgotPasswordRandomStringToEmail(User user, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vladstoroschuk1@gmail.com");
        message.setTo(user.getEmail());
        message.setText(mailMessage(verificationCode, user));
        message.setSubject("Reset Password Code");
        mailSender.send(message);
    }

    private String mailMessage(String verificationCode, User user) {
        return "Dear" + user.getFirstName() + " "+ user.getLastName() +",\n"
                + "Here is code to reset and change your password:\n"
                + verificationCode
                + "\nCode expire in 60 minutes.\n"
                + "Thank you,\n"
                + "VladEntertainment.";
    }
}
