package ua.com.foxminded.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import ua.com.foxminded.security.CustomUserDetails;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final Logger log = LogManager.getLogger();
    private static final String ROLE_PREFIX = "ROLE_";

    @Bean
    public SecurityFilterChain publicFilterChain(HttpSecurity http, AuthenticationSuccessHandler customSuccessHandler)
            throws Exception {

        http.authorizeHttpRequests(auth -> auth.mvcMatchers("/css/**", "/js/**").permitAll().mvcMatchers("/login")
                .permitAll()
                .mvcMatchers(WebRoutes.ACTIVATION_PATH + "/**").permitAll()
                .mvcMatchers(WebRoutes.PASSWORD_RESET_PATH + "/**").permitAll()
                .mvcMatchers(WebRoutes.REGISTRATION_COMPLETION_PATH + "/**").authenticated()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .mvcMatchers("/teacher/**").access(CustomAuthorizationManager.hasAnyRole("TEACHER", "ADMIN"))
                .mvcMatchers("/student/**").access(CustomAuthorizationManager.hasAnyRole("STUDENT", "ADMIN"))
                
                .mvcMatchers(HttpMethod.GET, "/api/v1/faculties/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/faculties/**").hasRole("ADMIN")
                
                .mvcMatchers(HttpMethod.GET, "/api/v1/departments/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/departments/**").hasRole("ADMIN")
                
                .mvcMatchers(HttpMethod.GET, "/api/v1/specializations/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/specializations/**").hasRole("ADMIN")
                
                .mvcMatchers(HttpMethod.GET, "/api/v1/classrooms/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/classrooms/**").access(CustomAuthorizationManager.hasAnyRole("TEACHER", "ADMIN"))
                
                .mvcMatchers(HttpMethod.GET, "/api/v1/groups/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/groups/**").access(CustomAuthorizationManager.hasAnyRole("TEACHER", "ADMIN"))

                .mvcMatchers(HttpMethod.GET, "/api/v1/lessons/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/lessons/**").access(CustomAuthorizationManager.hasAnyRole("TEACHER", "ADMIN"))

                .mvcMatchers(HttpMethod.GET, "/api/v1/students/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/students/**").access(CustomAuthorizationManager.hasAnyRole("TEACHER", "ADMIN"))
                
                .mvcMatchers(HttpMethod.GET, "/api/v1/subjects/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/subjects/**").access(CustomAuthorizationManager.hasAnyRole("TEACHER", "ADMIN"))

                .mvcMatchers(HttpMethod.GET, "/api/v1/teachers/**").access(CustomAuthorizationManager.authenticated())
                .mvcMatchers("/api/v1/teachers/**").hasRole("ADMIN")
                
                .mvcMatchers("/api/v1/**").denyAll()
                
                .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").successHandler(customSuccessHandler)
                        .failureUrl("/login?error").permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {

        Map<String, String> roleRedirectMap = new LinkedHashMap<>();
        roleRedirectMap.put("ADMIN", "/admin/dashboard");
        roleRedirectMap.put("TEACHER", "/teacher/dashboard");
        roleRedirectMap.put("STUDENT", "/student/dashboard");

        Set<String> rolesToSkipRegistrationCheck = new HashSet<>();
        rolesToSkipRegistrationCheck.add("ADMIN");

        return (request, response, authentication) -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            for (Map.Entry<String, String> entry : roleRedirectMap.entrySet()) {
                String expectedAuthority = ROLE_PREFIX + entry.getKey();
                if (authorities.stream().anyMatch(a -> a.getAuthority().equals(expectedAuthority))) {
                    boolean skipRegistrationCheck = rolesToSkipRegistrationCheck.contains(entry.getKey());

                    if (!skipRegistrationCheck && !userDetails.isRegistrationCompleted()) {
                        response.sendRedirect(WebRoutes.REGISTRATION_COMPLETION_PATH);
                    } else {
                        response.sendRedirect(entry.getValue());
                    }
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
