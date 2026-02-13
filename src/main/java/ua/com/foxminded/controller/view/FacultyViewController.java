package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.FacultyMapper;
import ua.com.foxminded.service.FacultyService;

@Controller
@RequestMapping("/faculties")
public class FacultyViewController {
    
    private final FacultyService service;
    private final FacultyMapper mapper;

    public FacultyViewController(FacultyService service, FacultyMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }
    
    @GetMapping
    public String listFaculties(Model model) {
        model.addAttribute("faculties", service.findAll().stream().map(mapper::toDto).collect(Collectors.toList()));
        return "faculty/list";
    }
}
