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

import ua.com.foxminded.dto.StudentDto;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }
    
    @GetMapping
    public Iterable<Student> findAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public Optional<Student> findByFullName(
            @RequestParam String lastName, 
            @RequestParam String firstName,
            @RequestParam String middleName) {
        return service.findByFullName(lastName, firstName, middleName);
    }

    @GetMapping("/{id}")
    public Student findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@RequestBody StudentDto dto) {
        return service.addOne(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Student update(@RequestBody StudentDto dto, @PathVariable Integer id) {
        return service.update(dto, id);
    }
}
