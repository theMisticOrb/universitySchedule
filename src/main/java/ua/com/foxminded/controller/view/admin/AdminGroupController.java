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

import ua.com.foxminded.dto.group.GroupCreateUpdateListDto;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.SpecializationRepository;
import ua.com.foxminded.service.GroupService;

@Controller
@RequestMapping("/admin/groups")
public class AdminGroupController {

    private final GroupService groupService;
    private final AcademicDepartmentRepository departmentRepository;
    private final SpecializationRepository specializationRepository;

    public AdminGroupController(GroupService groupService, AcademicDepartmentRepository departmentRepository,
            SpecializationRepository specializationRepository) {
        this.groupService = groupService;
        this.departmentRepository = departmentRepository;
        this.specializationRepository = specializationRepository;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("specializations", specializationRepository.findAll());
        return "admin/add-group";
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody GroupCreateUpdateListDto request) {
        try {
            groupService.addAll(request.getList());
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
