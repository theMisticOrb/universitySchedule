package ua.com.foxminded.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.group.GroupResponseDto;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.service.GroupService;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupRestController {

    private final GroupService service;
    private final GroupMapper mapper;

    public GroupRestController(GroupService service, GroupMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<GroupResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/name/{name:.+}")
    public GroupResponseDto findByName(@PathVariable String name) {
        return mapper.toResponseDto(service.findByName(name));
    }

    @GetMapping("/{id}")
    public GroupResponseDto findOne(@PathVariable Integer id) {
        return mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponseDto create(@RequestBody GroupCreateUpdateDto dto) {
        return mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public GroupResponseDto update(@RequestBody GroupCreateUpdateDto dto, @PathVariable Integer id) {
        return mapper.toResponseDto(service.update(dto, id));
    }

}
