package ua.com.foxminded.controller.view.validation;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.dto.email.PasswordResetDto;
import ua.com.foxminded.dto.email.PasswordResetRequestDto;
import ua.com.foxminded.service.ActivationUserService;

@Controller
@RequestMapping("/forgot-password")
public class PasswordResetController {

    private final ActivationUserService activationUserService;

    public PasswordResetController(ActivationUserService activationUserService) {
        this.activationUserService = activationUserService;
    }

    @GetMapping
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("request", new PasswordResetRequestDto());
        return "auth/forgot-password";
    }

    @PostMapping
    public String processForgotPassword(@Valid @ModelAttribute("request") PasswordResetRequestDto request,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/forgot-password";
        }

        try {
            activationUserService.requestPasswordReset(request.getEmail());
            return "auth/forgot-password-confirmation";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/forgot-password";
        }
    }

    @GetMapping("/reset")
    public String showResetForm(@RequestParam String tokenValue, Model model) {
        try {
            activationUserService.validateToken(tokenValue);
            model.addAttribute("token", tokenValue);
            model.addAttribute("request", new PasswordResetDto());
            return "auth/reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "activation-error";
        }
    }

    @PostMapping("/reset")
    public String processReset(@RequestParam String tokenValue,
            @Valid @ModelAttribute("request") PasswordResetDto request, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("token", tokenValue);
            return "auth/reset-password";
        }

        try {
            activationUserService.resetPassword(tokenValue, request.getPassword(), request.getConfirmPassword());
            return "redirect:/login?passwordReset=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("token", tokenValue);
            return "auth/reset-password";
        }
    }
}
