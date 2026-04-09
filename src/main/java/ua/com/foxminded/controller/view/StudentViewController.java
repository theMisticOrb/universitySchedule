package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentViewController {

    private final StudentService service;
    private final StudentMapper mapper;

    public StudentViewController(StudentService service, StudentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "student/list";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = service.findByEmail(userDetails.getUsername());
        model.addAttribute("currentUser", user);
        return "student/dashboard";
    }
}
