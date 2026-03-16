package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.service.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonViewController {

    private final LessonService service;
    private final LessonMapper mapper;

    public LessonViewController(LessonService service, LessonMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listLessons(Model model) {
        model.addAttribute("lessons",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "lesson/list";
    }
}
