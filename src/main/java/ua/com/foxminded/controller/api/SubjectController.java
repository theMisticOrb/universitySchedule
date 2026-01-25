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

import ua.com.foxminded.dto.SubjectDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.service.SubjectService;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService service;

    public SubjectController(SubjectService service) {
        this.service = service;
    }
    
    @GetMapping
    public Iterable<Subject> findAll() {
        return service.findAll();
    }

    @GetMapping("/title/{title}")
    public Optional<Subject> findByTitle(@PathVariable String title) {
        return service.findByTitle(title);
    }

    @GetMapping("/{id}")
    public Subject findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subject create(@RequestBody SubjectDto dto) {
        return service.addOne(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Subject updateDepartment(@RequestBody SubjectDto dto, @PathVariable Integer id) {
        return service.update(dto, id);
    }

}
