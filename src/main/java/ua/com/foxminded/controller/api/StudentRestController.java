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

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentResponseDto;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.service.StudentService;

@RestController
@RequestMapping("/api/v1/students")
public class StudentRestController {

    private final StudentService service;
    private final StudentMapper mapper;

    public StudentRestController(StudentService service, StudentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<StudentResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public StudentResponseDto findByFullName(@RequestParam String lastName, @RequestParam String firstName,
            @RequestParam String middleName) {
        return  mapper.toResponseDto(service.findByFullName(lastName, firstName, middleName));
    }

    @GetMapping("/{id}")
    public StudentResponseDto findOne(@PathVariable Integer id) {
        return  mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponseDto create(@RequestBody StudentCreateUpdateDto dto) {
        return  mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public StudentResponseDto update(@RequestBody StudentCreateUpdateDto dto, @PathVariable Integer id) {
        return  mapper.toResponseDto(service.fullUpdate(dto, id));
    }
}
