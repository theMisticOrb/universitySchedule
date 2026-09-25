package ua.com.foxminded.controller.view.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentsController {

    private final StudentService studentService;
    private final GroupService groupService;

    public AdminStudentsController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String findStudentsAndGroups(Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("groups", groupService.findAll());
        return "admin/students";
    }

    @PostMapping("/{id}/group")
    public String changeStudentGroup(@PathVariable Integer id, @RequestParam Integer groupId) {
        studentService.changeStudentGroup(id, groupId);
        return "redirect:/admin/students";
    }
}
