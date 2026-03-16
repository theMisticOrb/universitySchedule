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

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.dto.classroom.ClassroomResponseDto;
import ua.com.foxminded.mapper.ClassroomMapper;
import ua.com.foxminded.service.ClassroomService;

@RestController
@RequestMapping("/api/v1/classrooms")
public class ClassroomRestController {

    private final ClassroomService service;
    private final ClassroomMapper mapper;

    public ClassroomRestController(ClassroomService service, ClassroomMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ClassroomResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/name/{name}")
    public ClassroomResponseDto findByName(@PathVariable String name) {
        return mapper.toResponseDto(service.findByName(name));
    }

    @GetMapping("/{id}")
    public ClassroomResponseDto findOne(@PathVariable Integer id) {
        return mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassroomResponseDto create(@RequestBody ClassroomCreateUpdateDto dto) {
        return mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public ClassroomResponseDto update(@RequestBody ClassroomCreateUpdateDto dto, @PathVariable Integer id) {
        return mapper.toResponseDto(service.update(dto, id));
    }
}
