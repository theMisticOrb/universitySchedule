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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.TeacherService;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    
    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }
    
    @GetMapping
    public Iterable<Teacher> findAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public Optional<Teacher> findByFullName(
            @RequestParam String lastName, 
            @RequestParam String firstName,
            @RequestParam String middleName) {
        return service.findByFullName(lastName, firstName, middleName);
    }

    @GetMapping("/{id}")
    public Teacher findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher create(@RequestBody TeacherCreateUpdateDto dto) {
        return service.addOne(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Teacher update(@RequestBody TeacherCreateUpdateDto dto, @PathVariable Integer id) {
        return service.update(dto, id);
    }

}
