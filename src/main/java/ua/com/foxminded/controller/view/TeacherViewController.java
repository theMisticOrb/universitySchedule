package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherViewController {

    private final TeacherService service;
    private final TeacherMapper mapper;

    public TeacherViewController(TeacherService service, TeacherMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listTeachers(Model model) {
        model.addAttribute("teachers",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "teacher/list";
    }

}
