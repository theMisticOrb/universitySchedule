package ua.com.foxminded.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.service.StudentService;

@Controller
public class AuthenticationController {

    private final StudentService studentService;

    public AuthenticationController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("dto", new StudentCreateUpdateDto());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute StudentCreateUpdateDto dto) {
        studentService.addOne(dto);
        return "redirect:/login?registered";
    }
}
