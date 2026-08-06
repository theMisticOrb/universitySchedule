package ua.com.foxminded.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.service.AcademicDepartmentService;
import ua.com.foxminded.service.TeacherService;

@Controller
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final TeacherService teacherService;
    private final AcademicDepartmentService departmentService;

    public AdminTeacherController(TeacherService teacherService, AcademicDepartmentService departmentService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
    }

    @GetMapping
    public String findTeachersAndDepartments(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "admin/teachers";
    }

    @PostMapping("/{id}/department")
    public String changeTeacherDepartment(@PathVariable Integer id, @RequestParam Integer departmentId) {
        teacherService.changeTeacherDepartment(id, departmentId);
        return "redirect:/admin/teachers";
    }
}
