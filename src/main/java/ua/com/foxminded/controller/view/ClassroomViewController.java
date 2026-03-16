package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.ClassroomMapper;
import ua.com.foxminded.service.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomViewController {

    private final ClassroomService service;
    private final ClassroomMapper mapper;

    public ClassroomViewController(ClassroomService service, ClassroomMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listClassrooms(Model model) {
        model.addAttribute("classrooms",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "classroom/list";
    }
}
