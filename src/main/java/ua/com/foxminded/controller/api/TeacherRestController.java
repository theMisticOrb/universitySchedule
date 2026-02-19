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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.dto.user.TeacherResponseDto;
import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.service.TeacherService;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherRestController {

    private final TeacherService service;
    private final TeacherMapper mapper;

    public TeacherRestController(TeacherService service, TeacherMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<TeacherResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public TeacherResponseDto findByFullName(@RequestParam String lastName, @RequestParam String firstName,
            @RequestParam String middleName) {
        return mapper.toResponseDto(service.findByFullName(lastName, firstName, middleName));
    }

    @GetMapping("/{id}")
    public TeacherResponseDto findOne(@PathVariable Integer id) {
        return mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherResponseDto create(@RequestBody TeacherCreateUpdateDto dto) {
        return mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public TeacherResponseDto update(@RequestBody TeacherCreateUpdateDto dto, @PathVariable Integer id) {
        return mapper.toResponseDto(service.update(dto, id));
    }

}
