package ua.com.foxminded.controller.view.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.AcademicDepartmentService;
import ua.com.foxminded.service.SubjectService;
import ua.com.foxminded.service.TeacherService;

@Controller
@RequestMapping("/admin/teachers")
public class AdminTeacherController {

    private final TeacherService teacherService;
    private final AcademicDepartmentService departmentService;
    private final SubjectService subjectService;

    public AdminTeacherController(TeacherService teacherService, AcademicDepartmentService departmentService,
            SubjectService subjectService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public String findTeachersAndDepartments(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("allSubjects", subjectService.findAll());
        return "admin/teachers";
    }

    @PostMapping("/{id}/department")
    public String changeTeacherDepartment(@PathVariable Integer id, @RequestParam Integer departmentId) {
        teacherService.changeTeacherDepartment(id, departmentId);
        return "redirect:/admin/teachers";
    }

    @GetMapping("/{id}/subjects")
    public String showAssignSubjectsForm(@PathVariable Integer id, Model model) {
        Teacher teacher = teacherService.findById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("subjectsData", subjectService.findAll());
        return "admin/teacher-subjects";
    }

    @PostMapping("/{id}/subjects/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> assignSubjectsBatch(@PathVariable Integer id,
            @RequestBody Map<String, List<String>> body) {
        try {
            List<String> subjectTitles = body.get("subjectTitles");
            teacherService.assignSubjects(id, subjectTitles);
            Map<String, String> success = new HashMap<>();
            success.put("redirect", "/admin/teachers");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{id}/subjects/remove")
    public String removeSubject(@PathVariable Integer id, @RequestParam String subjectTitle) {
        teacherService.removeSubjectFromTeacher(id, subjectTitle);
        return "redirect:/admin/teachers/" + id + "/subjects";
    }
}
