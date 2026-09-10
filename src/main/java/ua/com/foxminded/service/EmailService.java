package ua.com.foxminded.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import ua.com.foxminded.config.WebRoutes;

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
        UriComponentsBuilder.fromHttpUrl(frontendUrl);
        String activationLink = UriComponentsBuilder.fromPath(WebRoutes.PASSWORD_RESET_PATH)
                .queryParam("tokenValue", token).toUriString();

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
        UriComponentsBuilder.fromHttpUrl(frontendUrl);
        String resetLink = UriComponentsBuilder.fromPath(WebRoutes.PASSWORD_RESET_PATH).queryParam("tokenValue", token)
                .toUriString();

        String subject = messageSource.getMessage("email.reset.subject", null, Locale.getDefault());
        String text = messageSource.getMessage("email.reset.text", new Object[] { resetLink }, Locale.getDefault());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
