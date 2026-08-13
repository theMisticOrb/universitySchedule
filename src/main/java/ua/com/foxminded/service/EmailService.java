package ua.com.foxminded.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final MessageSource messageSource;
    private final JavaMailSender mailSender;

    @Value("${university.frontend-url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender, MessageSource messageSource) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }

    public void sendActivationEmail(String toEmail, String token) {
        String activationLink = frontendUrl + "/activation/validate?tokenValue=" + token;

        String subject = messageSource.getMessage("email.activation.subject", null, Locale.getDefault());
        String text = messageSource.getMessage("email.activation.text", new Object[] { activationLink },
                Locale.getDefault());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetLink = frontendUrl + "/reset-password?tokenValue=" + token;

        String subject = messageSource.getMessage("email.reset.subject", null, Locale.getDefault());
        String text = messageSource.getMessage("email.reset.text", new Object[] { resetLink }, Locale.getDefault());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
