package ua.com.foxminded.controller.api;

import java.util.Optional;

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

import ua.com.foxminded.dto.SpecializationDto;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.service.SpecializationService;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    
    private final SpecializationService service;

    public SpecializationController(SpecializationService service) {
        this.service = service;
    }
    
    @GetMapping
    public Iterable<Specialization> findAll() {
        return service.findAll();
    }

    @GetMapping("/title/{title}")
    public Optional<Specialization> findByTitle(@PathVariable String title) {
        return service.findByTitle(title);
    }

    @GetMapping("/{id}")
    public Specialization findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Specialization create(@RequestBody SpecializationDto dto) {
        return service.addOne(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Specialization update(@RequestBody SpecializationDto dto, @PathVariable Integer id) {
        return service.update(dto, id);
    }
}
