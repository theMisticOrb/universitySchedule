package ua.com.foxminded.controller.view.validation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

}
