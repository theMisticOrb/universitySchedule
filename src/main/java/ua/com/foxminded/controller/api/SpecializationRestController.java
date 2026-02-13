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

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.specialization.SpecializationResponseDto;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationRestController {

    private final SpecializationService service;
    private final SpecializationMapper mapper;

    public SpecializationRestController(SpecializationService service, SpecializationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<SpecializationResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping("/title/{title}")
    public SpecializationResponseDto findByTitle(@PathVariable String title) {
        return mapper.toResponseDto(service.findByTitle(title));
    }

    @GetMapping("/{id}")
    public SpecializationResponseDto findOne(@PathVariable Integer id) {
        return  mapper.toResponseDto(service.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpecializationResponseDto create(@RequestBody SpecializationCreateUpdateDto dto) {
        return  mapper.toResponseDto(service.addOne(dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public SpecializationResponseDto update(@RequestBody SpecializationCreateUpdateDto dto, @PathVariable Integer id) {
        return  mapper.toResponseDto(service.update(dto, id));
    }
}
