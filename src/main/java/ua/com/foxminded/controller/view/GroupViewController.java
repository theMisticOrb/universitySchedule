package ua.com.foxminded.controller.view;

import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.service.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupViewController {

    private final GroupService service;
    private final GroupMapper mapper;

    public GroupViewController(GroupService service, GroupMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public String listGroups(Model model) {
        model.addAttribute("groups",
                service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList()));
        return "group/list";
    }

}
