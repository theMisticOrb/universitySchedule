package ua.com.foxminded.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${university.frontend-url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    public void sendActivationEmail(String toEmail, String token) {
        String activationLink = frontendUrl + "/activation/validate?tokenValue=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Account activation");
        message.setText(
            "You have been added to the system..\n\n" +
            "Follow the link to confirm your email and set a password:\n" +
            activationLink + "\n\n" +
            "The link is valid for 48 hours."
        );
        mailSender.send(message);
    }
}
