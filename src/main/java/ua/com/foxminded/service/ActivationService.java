package ua.com.foxminded.service;

import java.time.LocalDateTime;

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
public class ActivationService {

    private final ActivationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ActivationService(ActivationTokenRepository tokenRepository, UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    private ActivationToken getValidToken(String tokenValue) {
        ActivationToken token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(TokenNotFoundException::new);

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException();
        }

        if (token.getUsedAt() != null) {
            throw new TokenAlreadyUsedException();
        }

        return token;
    }

}
