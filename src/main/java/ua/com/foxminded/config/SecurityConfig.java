package ua.com.foxminded.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.mvcMatchers("/css/**", "/js/**", "/images/**", "/login", "/error")
                .permitAll().anyRequest().authenticated())
                .formLogin(form -> form.defaultSuccessUrl("/", true).permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }

}
