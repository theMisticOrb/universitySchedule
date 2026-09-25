package ua.com.foxminded.controller.view.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.TeacherService;
import ua.com.foxminded.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AdminPanelController(UserService userService, StudentService studentService, TeacherService teacherService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        AppUser user = userDetails.as(AppUser.class);

        model.addAttribute("totalStudents", studentService.count());
        model.addAttribute("totalTeachers", teacherService.count());
        model.addAttribute("totalUsers", userService.count());
        model.addAttribute("currentUser", user);
        return "admin/dashboard";
    }

}
