package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.service.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectViewController {

    private final SubjectService service;
    private final SubjectMapper mapper;

    public SubjectViewController(SubjectService service, SubjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listSubjects(Model model) {
        model.addAttribute("subjects",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "subject/list";
    }

}
