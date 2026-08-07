package ua.com.foxminded.controller.view.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateListDto;
import ua.com.foxminded.service.FacultyService;

@Controller
@RequestMapping("/admin/faculties")
public class AdminFacultiesController {

    private final FacultyService facultyService;

    public AdminFacultiesController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/add")
    public String showForm() {
        return "admin/add-faculty";
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody FacultyCreateUpdateListDto requestForm) {
        try {
            facultyService.createFaculties(requestForm.getList());
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
