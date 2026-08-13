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

import ua.com.foxminded.dto.user.ActivationRequestDto;
import ua.com.foxminded.exception.TokenExpiredException;
import ua.com.foxminded.service.ActivationUserService;

@Controller
@RequestMapping("/activation")
public class ActivationTokenController {

    private final ActivationUserService activationService;
    private static final String TOKEN = "token";

    public ActivationTokenController(ActivationUserService activationService) {
        this.activationService = activationService;
    }

    @GetMapping("/validate")
    public String showForm(@RequestParam String tokenValue, Model model) {
        try {
            activationService.validateToken(tokenValue);
            model.addAttribute(TOKEN, tokenValue); 
            model.addAttribute("request", new ActivationRequestDto());
            return "activation"; 
        } catch (TokenExpiredException ex) {
            model.addAttribute("error", "Ссылка устарела. Запросите новую.");
            return "activation-error"; 
        }
    }

    @PostMapping
    public String submit(@RequestParam String tokenValue, @Valid @ModelAttribute ActivationRequestDto request,
            BindingResult bindingResult, 
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(TOKEN, tokenValue);
            return "activation"; 
        }

        try {
            activationService.activateUser(tokenValue, request);
            return "redirect:/login?activated=true"; 
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute(TOKEN, tokenValue);
            return "activation";
        }
    }
}
