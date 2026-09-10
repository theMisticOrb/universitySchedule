package ua.com.foxminded.controller.view.validation;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.config.WebRoutes;
import ua.com.foxminded.dto.user.ActivationRequestDto;
import ua.com.foxminded.service.ActivationUserService;

@Controller
@RequestMapping(WebRoutes.ACTIVATION_PATH)
public class ActivationTokenController {

    private final ActivationUserService activationService;
    private static final String TOKEN = "token";

    public ActivationTokenController(ActivationUserService activationService) {
        this.activationService = activationService;
    }

    @GetMapping
    public String showForm(@RequestParam String tokenValue, Model model) {
        try {
            activationService.validateToken(tokenValue);
            model.addAttribute(TOKEN, tokenValue);
            model.addAttribute("request", new ActivationRequestDto());
            return "auth/activation";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute(TOKEN, tokenValue);
            return "auth/activation-error";
        }
    }

    @PostMapping("/validate")
    public String submit(@RequestParam("tokenValue") String tokenValue,
            @Valid @ModelAttribute("request") ActivationRequestDto request, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(TOKEN, tokenValue);
            model.addAttribute("request", request);
            return "auth/activation";
        }

        try {
            activationService.activateUser(tokenValue, request);
            return "redirect:/login?activated=true";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute(TOKEN, tokenValue);
            model.addAttribute("request", request);
            return "auth/activation-error";
        }
    }
}
