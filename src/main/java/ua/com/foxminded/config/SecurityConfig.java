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
        
        http.authorizeHttpRequests(auth -> auth.mvcMatchers("/css/**", "/js/**").permitAll()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/activation/**").permitAll()
                .mvcMatchers("/forgot-password", "/forgot-password/reset").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .mvcMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN")
                .mvcMatchers("/student/**").hasAnyRole("STUDENT", "ADMIN").anyRequest().authenticated())
        .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll());
        return http.build();
    }

}
