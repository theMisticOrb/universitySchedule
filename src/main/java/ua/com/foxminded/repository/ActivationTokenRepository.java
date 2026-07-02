package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.entity.ActivationToken;
import ua.com.foxminded.entity.AppUser;

@Repository
public interface ActivationTokenRepository extends JpaRepository<ActivationToken, Long> {
    
    Optional<ActivationToken> findByToken(String token);
    ActivationToken deleteByUser(AppUser user);

}
