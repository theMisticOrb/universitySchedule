package ua.com.foxminded.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class AuthenticationConfig {
    
    private static final Logger log = LogManager.getLogger();

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        
        Map<String, String> roleRedirectMap = new LinkedHashMap<>();
        roleRedirectMap.put("ADMIN", "/admin/dashboard");
        roleRedirectMap.put("TEACHER", "/teacher/dashboard");
        roleRedirectMap.put("STUDENT", "/student/dashboard");
        
        
        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (Map.Entry<String, String> entry : roleRedirectMap.entrySet()) {
                if (authorities.stream().anyMatch(a -> a.getAuthority().equals(entry.getKey()))) {
                    response.sendRedirect(entry.getValue());
                    return;
                }
            }
            
            log.warn("Unknown role: {}", authorities);
            response.sendRedirect("/");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
