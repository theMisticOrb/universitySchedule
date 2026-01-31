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

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectResponseDto;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.service.SubjectService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService service;
    private final SubjectMapper mapper;

    public SubjectController(SubjectService service, SubjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<SubjectResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/title/{title}")
    public SubjectResponseDto findByTitle(@PathVariable String title) {
        return  mapper.toDto(service.findByTitle(title));
    }

    @GetMapping("/{id}")
    public SubjectResponseDto findOne(@PathVariable Integer id) {
        return  mapper.toDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectResponseDto create(@RequestBody SubjectCreateUpdateDto dto) {
        return  mapper.toDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public SubjectResponseDto update(@RequestBody SubjectCreateUpdateDto dto, @PathVariable Integer id) {
        return  mapper.toDto(service.update(dto, id));
    }

}
