package ua.com.foxminded.service;

import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.com.foxminded.dto.user.ActivationRequestDto;
import ua.com.foxminded.dto.user.RegistrationCompletionDto;
import ua.com.foxminded.entity.ActivationToken;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.exception.TokenAlreadyUsedException;
import ua.com.foxminded.exception.TokenExpiredException;
import ua.com.foxminded.exception.TokenNotFoundException;
import ua.com.foxminded.repository.ActivationTokenRepository;
import ua.com.foxminded.repository.UserRepository;

@Service
@Transactional
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
        char[] password = request.getPassword();
        char[] confirmPassword = request.getConfirmPassword();

        try {

            ActivationToken token = getValidToken(tokenValue);

            if (!Arrays.equals(password, confirmPassword)) {
                throw new IllegalArgumentException("Passwords are not matching.");
            }

            AppUser user = token.getUser();

            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
            user.setRegistrationCompleted(true);
            userRepository.save(user);

            token.setUsedAt(LocalDateTime.now());
            tokenRepository.save(token);
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmPassword, '\0');
        }
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
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        String token = activationTokenService.createToken(user);
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    public void resetPassword(String tokenValue, char[] newPassword, char[] confirmPassword) {

        try {
            if (!Arrays.equals(newPassword, confirmPassword)) {
                throw new IllegalArgumentException("Passwords are not matching.");
            }
            ActivationToken token = getValidToken(tokenValue);
            AppUser user = token.getUser();
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(newPassword)));
            userRepository.save(user);
            token.setUsedAt(LocalDateTime.now());
            tokenRepository.save(token);
        } finally {
            Arrays.fill(newPassword, '\0');
            Arrays.fill(confirmPassword, '\0');
        }
    }

    public void completeRegistration(Integer userId, RegistrationCompletionDto request) {
        char[] password = request.getPassword();
        char[] confirmPassword = request.getConfirmPassword();

        try {
            if (!Arrays.equals(password, confirmPassword)) {
                throw new IllegalArgumentException("Passwords are not matching.");
            }

            AppUser user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found!"));

            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
            user.setRegistrationCompleted(true);

            userRepository.save(user);
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmPassword, '\0');
        }
    }

}
