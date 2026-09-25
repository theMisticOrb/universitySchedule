package ua.com.foxminded.controller.view.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.config.WebRoutes;
import ua.com.foxminded.exception.TokenExpiredException;
import ua.com.foxminded.service.ActivationUserService;

@WebMvcTest(controllers = PasswordResetController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class PasswordResetControllerTest {

    private static final String TOKEN_VALUE = "token-123";
    private static final String EMAIL = "user@example.com";
    private static final String PASSWORD = "SecurePass123";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivationUserService activationUserService;

    @Test
    void showForgotPasswordForm_shouldReturnViewWithEmptyRequest_whenAnonymous() throws Exception {
        mockMvc.perform(get("/forgot-password")).andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password")).andExpect(model().attributeExists("request"));
    }

    @Test
    void processForgotPassword_shouldReRenderForm_whenValidationFails() throws Exception {
        mockMvc.perform(post("/forgot-password").with(csrf())).andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"));

        verify(activationUserService, never()).requestPasswordReset(any());
    }

    @Test
    void processForgotPassword_shouldReturnConfirmationView_whenRequestSucceeds() throws Exception {
        mockMvc.perform(post("/forgot-password").with(csrf()).param("email", EMAIL)).andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password-confirmation"));

        verify(activationUserService, times(1)).requestPasswordReset(EMAIL);
    }

    @Test
    void processForgotPassword_shouldReRenderFormWithError_whenUserNotFound() throws Exception {
        EntityNotFoundException exception = new EntityNotFoundException("User not found!");
        doThrow(exception).when(activationUserService).requestPasswordReset(EMAIL);

        mockMvc.perform(post("/forgot-password").with(csrf()).param("email", EMAIL)).andExpect(status().isOk())
                .andExpect(view().name("auth/forgot-password"))
                .andExpect(model().attribute("error", exception.getMessage()));
    }

    @Test
    void processForgotPassword_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/forgot-password").param("email", EMAIL)).andExpect(status().isForbidden());
    }

    @Test
    void showResetForm_shouldReturnViewWithTokenAndEmptyRequest_whenTokenValid() throws Exception {
        mockMvc.perform(get(WebRoutes.PASSWORD_RESET_PATH + "/reset").param("tokenValue", TOKEN_VALUE))
                .andExpect(status().isOk()).andExpect(view().name("auth/reset-password"))
                .andExpect(model().attribute("token", TOKEN_VALUE)).andExpect(model().attributeExists("request"));

        verify(activationUserService, times(1)).validateToken(TOKEN_VALUE);
    }

    @Test
    void showResetForm_shouldReturnErrorView_whenTokenInvalid() throws Exception {
        TokenExpiredException exception = new TokenExpiredException();
        doThrow(exception).when(activationUserService).validateToken(TOKEN_VALUE);

        mockMvc.perform(get("/forgot-password/reset").param("tokenValue", TOKEN_VALUE)).andExpect(status().isOk())
                .andExpect(view().name("auth/activation-error"))
                .andExpect(model().attribute("error", exception.getMessage()));
    }

    @Test
    void processReset_shouldReRenderFormWithToken_whenValidationFails() throws Exception {
        mockMvc.perform(post("/forgot-password/reset").with(csrf()).param("tokenValue", TOKEN_VALUE))
                .andExpect(status().isOk()).andExpect(view().name("auth/reset-password"))
                .andExpect(model().attribute("token", TOKEN_VALUE));

        verify(activationUserService, never()).resetPassword(any(), any(), any());
    }

    @Test
    void processReset_shouldRedirectToLogin_whenResetSucceeds() throws Exception {
        mockMvc.perform(post("/forgot-password/reset").with(csrf()).param("tokenValue", TOKEN_VALUE)
                .param("password", PASSWORD).param("confirmPassword", PASSWORD)).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?passwordReset=true"));

        verify(activationUserService, times(1)).resetPassword(TOKEN_VALUE, PASSWORD.toCharArray(),
                PASSWORD.toCharArray());
    }

    @Test
    void processReset_shouldReRenderFormWithError_whenServiceThrows() throws Exception {
        IllegalArgumentException exception = new IllegalArgumentException("Passwords are not matching.");
        doThrow(exception).when(activationUserService).resetPassword(eq(TOKEN_VALUE), any(), any());

        mockMvc.perform(post("/forgot-password/reset").with(csrf()).param("tokenValue", TOKEN_VALUE)
                .param("password", PASSWORD).param("confirmPassword", "DifferentPass456")).andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"))
                .andExpect(model().attribute("error", exception.getMessage()))
                .andExpect(model().attribute("token", TOKEN_VALUE));
    }

    @Test
    void processReset_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/forgot-password").param("tokenValue", TOKEN_VALUE).param("password", PASSWORD)
                .param("confirmPassword", PASSWORD)).andExpect(status().isForbidden());
    }

}
