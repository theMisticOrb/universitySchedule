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

import ua.com.foxminded.dto.specialization.SpecializationCreateUndateListDto;
import ua.com.foxminded.repository.SpecializationRepository;
import ua.com.foxminded.service.SpecializationService;

@Controller
@RequestMapping("/admin/specializations")
public class AdminSpecializationsController {

    private final SpecializationRepository specializationRepository;
    private final SpecializationService specializationService;

    public AdminSpecializationsController(SpecializationRepository specializationRepository,
            SpecializationService specializationService) {
        this.specializationRepository = specializationRepository;
        this.specializationService = specializationService;
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("specializations", specializationRepository.findAll());
        return "admin/add-specialization";
    }

    @PostMapping("/batch")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveBatch(@RequestBody SpecializationCreateUndateListDto request) {
        try {
            specializationService.createSpecializations(request.getList());
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
