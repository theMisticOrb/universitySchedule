package ua.com.foxminded.controller.view.validation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.dto.user.ActivationRequestDto;
import ua.com.foxminded.exception.TokenAlreadyUsedException;
import ua.com.foxminded.exception.TokenExpiredException;
import ua.com.foxminded.exception.TokenNotFoundException;
import ua.com.foxminded.service.ActivationUserService;

@WebMvcTest(controllers = ActivationTokenController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class ActivationTokenControllerTest {

    private static final String TOKEN_VALUE = "token-123";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivationUserService activationService;

    @Test
    void showForm_shouldReturnActivationView_whenTokenIsValid_andAnonymous() throws Exception {
        mockMvc.perform(get("/activation/validate").param("tokenValue", TOKEN_VALUE)).andExpect(status().isOk())
                .andExpect(view().name("auth/activation")).andExpect(model().attribute("token", TOKEN_VALUE))
                .andExpect(model().attributeExists("request"));

        verify(activationService, times(1)).validateToken(TOKEN_VALUE);
    }

    @Test
    void showForm_shouldReturnErrorView_whenTokenExpired() throws Exception {
        doThrow(new TokenExpiredException()).when(activationService).validateToken(TOKEN_VALUE);

        mockMvc.perform(get("/activation/validate").param("tokenValue", TOKEN_VALUE)).andExpect(status().isOk())
                .andExpect(view().name("auth/activation-error"))
                .andExpect(model().attribute("error", "Token has been expired."));
    }

    @Test
    void showForm_shouldReturnBadRequest_whenTokenNotFound() throws Exception {
        doThrow(new TokenNotFoundException()).when(activationService).validateToken(TOKEN_VALUE);

        mockMvc.perform(get("/activation/validate").param("tokenValue", TOKEN_VALUE)).andExpect(status().isOk())
                .andExpect(view().name("auth/activation-error")).andExpect(model().attribute("token", TOKEN_VALUE))
                .andExpect(model().attributeExists("error"));

        verify(activationService, times(1)).validateToken(TOKEN_VALUE);
    }

    @Test
    void showForm_shouldReturnBadRequest_whenTokenAlreadyUsed() throws Exception {
        doThrow(new TokenAlreadyUsedException()).when(activationService).validateToken(TOKEN_VALUE);

        mockMvc.perform(get("/activation/validate").param("tokenValue", TOKEN_VALUE)).andExpect(status().isOk())
                .andExpect(view().name("auth/activation-error")).andExpect(model().attribute("token", TOKEN_VALUE))
                .andExpect(model().attributeExists("error"));

        verify(activationService, times(1)).validateToken(TOKEN_VALUE);
    }

    @Test
    void submit_shouldReRenderFormWithoutCallingService_whenValidationFails() throws Exception {
        mockMvc.perform(post("/activation").with(csrf()).param("tokenValue", TOKEN_VALUE)).andExpect(status().isOk())
                .andExpect(view().name("auth/activation")).andExpect(model().attribute("token", TOKEN_VALUE));

        verify(activationService, org.mockito.Mockito.never()).activateUser(any(), any());
    }

    @Test
    void submit_shouldRedirectToLogin_whenActivationSucceeds() throws Exception {
        mockMvc.perform(
                post("/activation").with(csrf()).param("tokenValue", TOKEN_VALUE).param("email", "user@example.com")
                        .param("password", "SecurePass123").param("confirmPassword", "SecurePass123"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login?activated=true"));

        verify(activationService, times(1)).activateUser(eq(TOKEN_VALUE), any(ActivationRequestDto.class));
    }

    @Test
    void submit_shouldReRenderFormWithError_whenServiceThrowsRuntimeException() throws Exception {
        doThrow(new TokenAlreadyUsedException()).when(activationService).activateUser(eq(TOKEN_VALUE),
                any(ActivationRequestDto.class));

        mockMvc.perform(
                post("/activation").with(csrf()).param("tokenValue", TOKEN_VALUE).param("email", "user@example.com")
                        .param("password", "SecurePass123").param("confirmPassword", "SecurePass123"))
                .andExpect(status().isOk()).andExpect(view().name("auth/activation-error"))
                .andExpect(model().attributeExists("error")).andExpect(model().attribute("token", TOKEN_VALUE));
    }

    @Test
    void submit_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/activation").param("tokenValue", TOKEN_VALUE).param("email", "user@example.com")
                .param("password", "SecurePass123")).andExpect(status().isForbidden());
    }

}
