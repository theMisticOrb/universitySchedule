package ua.com.foxminded.controller.view;

import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.AcademicDepartmentService;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.TeacherService;
import ua.com.foxminded.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final AcademicDepartmentService departmentService;

    public AdminController(UserService userService, StudentService studentService, TeacherService teacherService,
            GroupService groupService, AcademicDepartmentService departmentService) {
        this.userService = userService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.departmentService = departmentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        Teacher user = userDetails.as(Teacher.class);
        
        model.addAttribute("totalStudents", studentService.count());
        model.addAttribute("totalTeachers", teacherService.count());
        model.addAttribute("totalUsers", userService.count());
        model.addAttribute("currentUser", user);
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", UserRole.values());
        return "admin/user-edit";
    }

    @PostMapping("/users/{id}/edit/roles")
    public String changeUserRole(@PathVariable Integer id, @RequestParam Set<UserRole> roles) {
        userService.changeRole(id, roles);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/students")
    public String findStudentsAndGroups(Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("groups", groupService.findAll());
        return "admin/students";
    }

    @GetMapping("/teachers")
    public String findTeachersAndDepartments(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/teachers";
    }

    @PostMapping("/students/{id}/group")
    public String changeStudentGroup(@PathVariable Integer id, @RequestParam Integer groupId) {
        studentService.changeStudentGroup(id, groupId);
        return "redirect:/admin/students";
    }

    @PostMapping("/teachers/{id}/department")
    public String changeTeacherDepartment(@PathVariable Integer id, @RequestParam Integer departmentId) {
        teacherService.changeTeacherDepartment(id, departmentId);
        return "redirect:/admin/teachers";
    }
}
