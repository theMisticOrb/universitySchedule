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

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateListDto;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.service.AcademicDepartmentService;

@Controller
@RequestMapping("/admin/departments")
public class AdminDepartmentsController {

    private final AcademicDepartmentService departmentService;
    private final FacultyRepository facultyRepository;

    public AdminDepartmentsController(AcademicDepartmentService departmentService,
            FacultyRepository facultyRepository) {
        this.departmentService = departmentService;
        this.facultyRepository = facultyRepository;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("faculties", facultyRepository.findAll());
        return "admin/add-department";
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody AcademicDepartmentCreateUpdateListDto request) {
        try {
            departmentService.createDepartments(request.getList());
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
