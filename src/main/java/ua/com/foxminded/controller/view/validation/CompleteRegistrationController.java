package ua.com.foxminded.controller.view.validation;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.config.WebRoutes;
import ua.com.foxminded.dto.user.RegistrationCompletionDto;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.ActivationUserService;

@Controller
@RequestMapping(WebRoutes.REGISTRATION_COMPLETION_PATH)
public class CompleteRegistrationController {

    private final ActivationUserService activationUserService;

    public CompleteRegistrationController(ActivationUserService activationUserService) {
        this.activationUserService = activationUserService;
    }

    @GetMapping
    public String showForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails.isRegistrationCompleted()) {
            return "redirect:/";
        }
        model.addAttribute("request", new RegistrationCompletionDto());
        return "auth/complete_registration";
    }

    @PostMapping
    public String complete(@Valid @ModelAttribute("request") RegistrationCompletionDto request,
            BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/complete_registration";
        }

        try {
            activationUserService.completeRegistration(userDetails.getUser().getId(), request);
            return "redirect:/login?registrationCompleted=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/complete_registration";
        }
    }

}
