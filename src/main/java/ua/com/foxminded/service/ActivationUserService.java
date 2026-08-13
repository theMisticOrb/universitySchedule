package ua.com.foxminded.service;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dto.user.ActivationRequestDto;
import ua.com.foxminded.entity.ActivationToken;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.exception.TokenAlreadyUsedException;
import ua.com.foxminded.exception.TokenExpiredException;
import ua.com.foxminded.exception.TokenNotFoundException;
import ua.com.foxminded.repository.ActivationTokenRepository;
import ua.com.foxminded.repository.UserRepository;

@Service
public class ActivationUserService {

    private final ActivationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationTokenService activationTokenService;
    private final EmailService emailService;

    public ActivationUserService(ActivationTokenRepository tokenRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder, ActivationTokenService activationTokenService, EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activationTokenService = activationTokenService;
        this.emailService = emailService;
    }

    public void validateToken(String tokenValue) {
        getValidToken(tokenValue);
    }

    public void activateUser(String tokenValue, ActivationRequestDto request) {
        ActivationToken token = getValidToken(tokenValue);

        AppUser user = token.getUser();

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRegistrationCompleted(true);

        userRepository.save(user);

        token.setUsedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

    public void sendResetEmail(AppUser user) {
        String token = activationTokenService.createToken(user);
        emailService.sendActivationEmail(user.getEmail(), token);

    }

    public void sendResetEmailIfTempPassword(AppUser user) {
        if (!user.isRegistrationCompleted()) {
            sendResetEmail(user);
        }
    }

    private ActivationToken getValidToken(String tokenValue) {
        ActivationToken token = tokenRepository.findByToken(tokenValue).orElseThrow(TokenNotFoundException::new);

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        if (token.getUsedAt() != null) {
            throw new TokenAlreadyUsedException();
        }

        return token;
    }
    
    public void requestPasswordReset(String email) {
        AppUser user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Користувача не знайдено"));
        String token = activationTokenService.createToken(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String tokenValue, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Паролі не співпадають");
        }
        ActivationToken token = getValidToken(tokenValue);
        AppUser user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        token.setUsedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

}
