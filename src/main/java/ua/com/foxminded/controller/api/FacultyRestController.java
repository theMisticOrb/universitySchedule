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

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.dto.faculty.FacultyResponseDto;
import ua.com.foxminded.mapper.FacultyMapper;
import ua.com.foxminded.service.FacultyService;

@RestController
@RequestMapping("/api/faculties")
public class FacultyRestController {

    private final FacultyService service;
    private final FacultyMapper mapper;

    public FacultyRestController(FacultyService service, FacultyMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<FacultyResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/title/{title}")
    public FacultyResponseDto findByTitle(@PathVariable String title) {
        return mapper.toResponseDto(service.findByTitle(title));
    }

    @GetMapping("/{id}")
    public FacultyResponseDto findOne(@PathVariable Integer id) {
        return mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacultyResponseDto create(@RequestBody FacultyCreateUpdateDto dto) {
        return mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public FacultyResponseDto update(@RequestBody FacultyCreateUpdateDto dto, @PathVariable Integer id) {
        return mapper.toResponseDto(service.update(dto, id));
    }
}
