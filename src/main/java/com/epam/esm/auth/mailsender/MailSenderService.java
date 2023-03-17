package com.epam.esm.auth.mailsender;

import com.epam.esm.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;

    public void sendForgotPasswordVerificationCodeToEmail(User user, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(user.getEmail());
        message.setText(getForgotPasswordMailMessage(verificationCode, user));
        message.setSubject("Reset Password Code");
        mailSender.send(message);
    }

    private String getForgotPasswordMailMessage(String verificationCode, User user) {
        return new StringBuilder()
                .append("Dear").append(user.getFirstName())
                .append(" ").append(user.getLastName()).append(",").append(System.getProperty("line.separator"))
                .append("Here is code to reset and change your password:").append(System.getProperty("line.separator"))
                .append(verificationCode).append(System.getProperty("line.separator"))
                .append("Code expire in 60 minutes.").append(System.getProperty("line.separator"))
                .append("Thank you,").append(System.getProperty("line.separator"))
                .append("CertificatesEntertainment.")
                .toString();
    }

    public void sendCreatedAccountMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(user.getEmail());
        message.setText(getCreatedAccountMessage(user));
        message.setSubject("Reset Password Code");
        mailSender.send(message);
    }

    private String getCreatedAccountMessage(User user) {
        return new StringBuilder().append("Dear").append(user.getFirstName())
                .append(" ").append(user.getLastName()).append(",").append(System.getProperty("line.separator"))
                .append("Your account created successfully:").append(System.getProperty("line.separator"))
                .append("Thank you,").append(System.getProperty("line.separator"))
                .append("CertificatesEntertainment.")
                .toString();
    }
}
