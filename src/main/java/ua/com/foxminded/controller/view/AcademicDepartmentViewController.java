package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.service.AcademicDepartmentService;

@Controller
@RequestMapping("/departments")
public class AcademicDepartmentViewController {

    private final AcademicDepartmentService service;
    private final AcademicDepartmentMapper mapper;

    public AcademicDepartmentViewController(AcademicDepartmentService service, AcademicDepartmentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "department/list";
    }

}
