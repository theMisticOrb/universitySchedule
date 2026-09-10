package ua.com.foxminded.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import ua.com.foxminded.config.WebRoutes;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    private static final String FRONTEND_URL = "http://localhost:4200";
    private static final String TO_EMAIL = "user@example.com";
    private static final String TOKEN = "token-123";
    private static final String ACTIVATION_SUBJECT = "Activate your account";
    private static final String ACTIVATION_TEXT = "Click the link to activate";
    private static final String RESET_SUBJECT = "Reset your password";
    private static final String RESET_TEXT = "Click the link to reset password";

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "frontendUrl", FRONTEND_URL);
    }

    @Test
    void sendActivationEmail_shouldSendMessageWithCorrectToAndSubject() {
        when(messageSource.getMessage(eq("email.activation.subject"), isNull(), any(Locale.class)))
            .thenReturn(ACTIVATION_SUBJECT);
        when(messageSource.getMessage(eq("email.activation.text"), any(Object[].class), any(Locale.class)))
            .thenReturn(ACTIVATION_TEXT);
 
        emailService.sendActivationEmail(TO_EMAIL, TOKEN);
 
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
 
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo()).containsExactly(TO_EMAIL);
        assertThat(sentMessage.getSubject()).isEqualTo(ACTIVATION_SUBJECT);
        assertThat(sentMessage.getText()).isEqualTo(ACTIVATION_TEXT);
    }

    @Test
    void sendActivationEmail_shouldBuildCorrectActivationLink() {
        when(messageSource.getMessage(eq("email.activation.subject"), isNull(), any(Locale.class)))
            .thenReturn(ACTIVATION_SUBJECT);
        when(messageSource.getMessage(eq("email.activation.text"), any(Object[].class), any(Locale.class)))
            .thenReturn(ACTIVATION_TEXT);
 
        emailService.sendActivationEmail(TO_EMAIL, TOKEN);
 
        ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);
        verify(messageSource).getMessage(eq("email.activation.text"), argsCaptor.capture(), any(Locale.class));
 
        
        UriComponentsBuilder.fromHttpUrl(FRONTEND_URL);
        String expectedLink = UriComponentsBuilder.fromPath(WebRoutes.ACTIVATION_PATH + "/validate")
                .queryParam("tokenValue", TOKEN).toUriString();
        assertThat(argsCaptor.getValue()).containsExactly(expectedLink);
    }

    @Test
    void sendPasswordResetEmail_shouldSendMessageWithCorrectToAndSubject() {
        when(messageSource.getMessage(eq("email.reset.subject"), isNull(), any(Locale.class)))
            .thenReturn(RESET_SUBJECT);
        when(messageSource.getMessage(eq("email.reset.text"), any(Object[].class), any(Locale.class)))
            .thenReturn(RESET_TEXT);
 
        emailService.sendPasswordResetEmail(TO_EMAIL, TOKEN);
 
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
 
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertThat(sentMessage.getTo()).containsExactly(TO_EMAIL);
        assertThat(sentMessage.getSubject()).isEqualTo(RESET_SUBJECT);
        assertThat(sentMessage.getText()).isEqualTo(RESET_TEXT);
    }

    @Test
    void sendPasswordResetEmail_shouldBuildCorrectResetLink() {
        when(messageSource.getMessage(eq("email.reset.subject"), isNull(), any(Locale.class)))
            .thenReturn(RESET_SUBJECT);
        when(messageSource.getMessage(eq("email.reset.text"), any(Object[].class), any(Locale.class)))
            .thenReturn(RESET_TEXT);
 
        emailService.sendPasswordResetEmail(TO_EMAIL, TOKEN);
 
        ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);
        verify(messageSource).getMessage(eq("email.reset.text"), argsCaptor.capture(), any(Locale.class));
 
        UriComponentsBuilder.fromHttpUrl(FRONTEND_URL);
        String expectedLink = UriComponentsBuilder.fromPath(WebRoutes.PASSWORD_RESET_PATH + "/reset-password")
                .queryParam("tokenValue", TOKEN).toUriString();
        assertThat(argsCaptor.getValue()).containsExactly(expectedLink);
    }

}
