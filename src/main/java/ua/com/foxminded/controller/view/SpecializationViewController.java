package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.service.SpecializationService;

@Controller
@RequestMapping("/specializations")
public class SpecializationViewController {

    private final SpecializationService service;
    private final SpecializationMapper mapper;

    public SpecializationViewController(SpecializationService service, SpecializationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listSpecializations(Model model) {
        model.addAttribute("specializations",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "specialization/list";
    }

}
