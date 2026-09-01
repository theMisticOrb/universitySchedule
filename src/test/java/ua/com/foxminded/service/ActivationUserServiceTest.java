package ua.com.foxminded.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import ua.com.foxminded.dto.user.ActivationRequestDto;
import ua.com.foxminded.entity.ActivationToken;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.exception.TokenAlreadyUsedException;
import ua.com.foxminded.exception.TokenExpiredException;
import ua.com.foxminded.exception.TokenNotFoundException;
import ua.com.foxminded.repository.ActivationTokenRepository;
import ua.com.foxminded.repository.UserRepository;
import ua.com.foxminded.service.suppport.PostgresTestContainer;

@ExtendWith(MockitoExtension.class)
class ActivationUserServiceTest {

    private static final String TOKEN_VALUE = "token-123";
    private static final String RAW_PASSWORD = "rawPassword";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String EMAIL = "user@example.com";

    @Mock
    private ActivationTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ActivationTokenService activationTokenService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ActivationUserService activationUserService;

    private ActivationToken validToken;
    private AppUser user;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = PostgresTestContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @BeforeEach
    void setUp() {
        user = mock(AppUser.class);
        validToken = mock(ActivationToken.class);
    }

    @Test
    void validateToken_shouldNotThrow_whenTokenIsValid() {
        stubToken(validToken, LocalDateTime.now().plusHours(1), null);
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(validToken));

        activationUserService.validateToken(TOKEN_VALUE);

        verify(tokenRepository).findByToken(TOKEN_VALUE);
    }

    @Test
    void validateToken_shouldThrowTokenNotFound_whenTokenMissing() {
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.empty());
 
        assertThatThrownBy(() -> activationUserService.validateToken(TOKEN_VALUE))
            .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    void validateToken_shouldThrowTokenExpired_whenTokenExpired() {
        when(validToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusMinutes(1));        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(validToken));

        assertThatThrownBy(() -> activationUserService.validateToken(TOKEN_VALUE))
                .isInstanceOf(TokenExpiredException.class);
    }

    @Test
    void validateToken_shouldThrowTokenAlreadyUsed_whenTokenUsed() {
        stubToken(validToken, LocalDateTime.now().plusHours(1), LocalDateTime.now().minusMinutes(5));
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(validToken));

        assertThatThrownBy(() -> activationUserService.validateToken(TOKEN_VALUE))
                .isInstanceOf(TokenAlreadyUsedException.class);
    }

    @Test
    void activateUser_shouldUpdateUserAndMarkTokenUsed_whenTokenValid() {
        stubToken(validToken, LocalDateTime.now().plusHours(1), null);
        when(validToken.getUser()).thenReturn(user);
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(validToken));
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        ActivationRequestDto request = mock(ActivationRequestDto.class);
        when(request.getEmail()).thenReturn(EMAIL);
        when(request.getPassword()).thenReturn(RAW_PASSWORD);

        activationUserService.activateUser(TOKEN_VALUE, request);

        verify(user).setEmail(EMAIL);
        verify(user).setPassword(ENCODED_PASSWORD);
        verify(user).setRegistrationCompleted(true);
        verify(userRepository).save(user);

        verify(validToken).setUsedAt(any(LocalDateTime.class));
        verify(tokenRepository).save(validToken);
    }

    @Test
    void activateUser_shouldThrowAndNotTouchUser_whenTokenInvalid() {
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.empty());
 
        ActivationRequestDto request = mock(ActivationRequestDto.class);
 
        assertThatThrownBy(() -> activationUserService.activateUser(TOKEN_VALUE, request))
            .isInstanceOf(TokenNotFoundException.class);
 
        verifyNoInteractions(userRepository, passwordEncoder);
    }

    @Test
    void sendResetEmail_shouldCreateTokenAndSendActivationEmail() {
        when(user.getEmail()).thenReturn(EMAIL);
        when(activationTokenService.createToken(user)).thenReturn(TOKEN_VALUE);
 
        activationUserService.sendResetEmail(user);
 
        verify(activationTokenService).createToken(user);
        verify(emailService).sendActivationEmail(EMAIL, TOKEN_VALUE);
    }

    @Test
    void sendResetEmailIfTempPassword_shouldSendEmail_whenRegistrationNotCompleted() {
        when(user.isRegistrationCompleted()).thenReturn(false);
        when(user.getEmail()).thenReturn(EMAIL);
        when(activationTokenService.createToken(user)).thenReturn(TOKEN_VALUE);
 
        activationUserService.sendResetEmailIfTempPassword(user);
 
        verify(activationTokenService).createToken(user);
        verify(emailService).sendActivationEmail(EMAIL, TOKEN_VALUE);
    }

    @Test
    void sendResetEmailIfTempPassword_shouldDoNothing_whenRegistrationCompleted() {
        when(user.isRegistrationCompleted()).thenReturn(true);
 
        activationUserService.sendResetEmailIfTempPassword(user);
 
        verifyNoInteractions(activationTokenService, emailService);
    }

    @Test
    void requestPasswordReset_shouldCreateTokenAndSendResetEmail_whenUserFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(EMAIL);
        when(activationTokenService.createToken(user)).thenReturn(TOKEN_VALUE);
 
        activationUserService.requestPasswordReset(EMAIL);
 
        verify(activationTokenService).createToken(user);
        verify(emailService).sendPasswordResetEmail(EMAIL, TOKEN_VALUE);
    }

    @Test
    void requestPasswordReset_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
 
        assertThatThrownBy(() -> activationUserService.requestPasswordReset(EMAIL))
            .isInstanceOf(EntityNotFoundException.class);
 
        verifyNoInteractions(activationTokenService, emailService);
    }

    @Test
    void resetPassword_shouldThrowIllegalArgument_whenPasswordsDoNotMatch() {
        assertThatThrownBy(() -> activationUserService.resetPassword(TOKEN_VALUE, "pass1", "pass2"))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(tokenRepository, userRepository, passwordEncoder);
    }

    @Test
    void resetPassword_shouldUpdatePasswordAndMarkTokenUsed_whenPasswordsMatchAndTokenValid() {
        stubToken(validToken, LocalDateTime.now().plusHours(1), null);
        when(validToken.getUser()).thenReturn(user);
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(validToken));
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        activationUserService.resetPassword(TOKEN_VALUE, RAW_PASSWORD, RAW_PASSWORD);

        verify(user).setPassword(ENCODED_PASSWORD);
        verify(userRepository).save(user);
        verify(validToken).setUsedAt(any(LocalDateTime.class));
        verify(tokenRepository).save(validToken);
    }

    @Test
    void resetPassword_shouldThrowTokenExpired_whenTokenExpired() {
        when(validToken.getExpiryDate()).thenReturn(LocalDateTime.now().minusMinutes(1));
        when(tokenRepository.findByToken(TOKEN_VALUE)).thenReturn(Optional.of(validToken));
        assertThatThrownBy(() -> activationUserService.resetPassword(TOKEN_VALUE, RAW_PASSWORD, RAW_PASSWORD))
                .isInstanceOf(TokenExpiredException.class);

        verifyNoInteractions(userRepository, passwordEncoder);
    }

    private void stubToken(ActivationToken token, LocalDateTime expiryDate, LocalDateTime usedAt) {
        when(token.getExpiryDate()).thenReturn(expiryDate);
        when(token.getUsedAt()).thenReturn(usedAt);
    }
}
