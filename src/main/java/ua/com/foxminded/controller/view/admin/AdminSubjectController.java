package ua.com.foxminded.controller.view.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateListDto;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.SubjectService;

@Controller
@RequestMapping("/admin/subjects")
public class AdminSubjectController {

    private final SubjectService subjectService;
    private final AcademicDepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    public AdminSubjectController(SubjectService subjectService, AcademicDepartmentRepository departmentRepository,
            TeacherRepository teacherRepository) {
        this.subjectService = subjectService;
        this.departmentRepository = departmentRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        return "admin/add-subject";
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody SubjectCreateUpdateListDto request) {
        try {
            subjectService.createSubjects(request.getList());
            Map<String, String> success = new HashMap<>();
            success.put("redirect", "/admin/dashboard");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
