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

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.dto.department.AcademicDepartmentResponseDto;
import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.service.AcademicDepartmentService;

@RestController
@RequestMapping("/api/departments")
public class AcademicDepartmentController {

    private final AcademicDepartmentService service;
    private final AcademicDepartmentMapper mapper;

    
    public AcademicDepartmentController(AcademicDepartmentService service, AcademicDepartmentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AcademicDepartmentResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/title/{title}")
    public AcademicDepartmentResponseDto findByTitle(@PathVariable String title) {
        return mapper.toResponseDto(service.findByTitle(title)) ;
    }

    @GetMapping("/{id}")
    public AcademicDepartmentResponseDto findOne(@PathVariable Integer id) {
        return mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicDepartmentResponseDto create(@RequestBody AcademicDepartmentCreateUpdateDto dto) {
        return mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public AcademicDepartmentResponseDto update(@RequestBody AcademicDepartmentCreateUpdateDto dto, @PathVariable Integer id) {
        return mapper.toResponseDto(service.update(dto, id));
    }

}
