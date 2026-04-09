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
                .mvcMatchers("/login", "/register").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .mvcMatchers("/teachers/**").hasAnyRole("TEACHER", "ADMIN")
                .mvcMatchers("/students/**").hasRole("STUDENT").anyRequest().authenticated())
        .formLogin(form -> form
                .loginPage("login")
                .defaultSuccessUrl("/",true)
                .failureUrl("/login?error")
                .permitAll());
        return http.build();
    }

}
