package ua.com.foxminded.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.com.foxminded.repository.UserRepository;

@Configuration
public class AuthenticationConfig {
//    @Bean
//    public UserDetailsService userDetailsService(UserRepository repo) {
//        return username -> repo.find(username)
//                .map(u -> User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRole()).build())
//                .orElseThrow(() -> new UsernameNotFoundException(username));
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
