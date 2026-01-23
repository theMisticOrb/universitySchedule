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

import ua.com.foxminded.dto.ClassroomDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.service.ClassroomService;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService service;

    public ClassroomController(ClassroomService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Classroom> findAll() {
        return service.findAll();
    }

    @GetMapping("/title/{title}")
    public Optional<Classroom> findByTitle(@PathVariable String title) {
        return service.findByTitle(title);
    }

    @GetMapping("/{id}")
    public Classroom findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Classroom create(@RequestBody ClassroomDto classroom) {
        return service.addOne(classroom);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Classroom updateDepartment(@RequestBody ClassroomDto clasroom, @PathVariable Integer id) {
        return service.update(clasroom, id);
    }
}
