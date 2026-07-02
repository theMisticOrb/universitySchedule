package ua.com.foxminded.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ua.com.foxminded.entity.ActivationToken;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.repository.ActivationTokenRepository;

@Service
public class TokenService {

    private final ActivationTokenRepository tokenRepo;

    public TokenService(ActivationTokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }
    
    public String createToken(AppUser user) {
        
        tokenRepo.deleteByUser(user);
        
        String tokenValue = UUID.randomUUID().toString();
        
        ActivationToken token = new ActivationToken();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusHours(48));
        
        tokenRepo.save(token);
        
        return tokenValue;
    }
    
    
}
