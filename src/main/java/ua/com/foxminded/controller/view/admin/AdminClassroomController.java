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

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDtoList;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.service.ClassroomService;

@Controller
@RequestMapping("/admin/classroom")
public class AdminClassroomController {

    private ClassroomRepository classroomRepository;
    private ClassroomService classroomService;

    public AdminClassroomController(ClassroomRepository classroomRepository, ClassroomService classroomService) {
        this.classroomRepository = classroomRepository;
        this.classroomService = classroomService;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("classrooms", classroomRepository.findAll());
        return "admin/add-classroom";
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody ClassroomCreateUpdateDtoList request) {
        try {
            classroomService.createClassrooms(request.getList());
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
