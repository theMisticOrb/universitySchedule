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

import ua.com.foxminded.dto.AcademicDepartmentDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.service.AcademicDepartmentService;

@RestController
@RequestMapping("/api/departments")
public class AcademicDepartmentController {

    private final AcademicDepartmentService departmentService;

    public AcademicDepartmentController(AcademicDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public Iterable<AcademicDepartment> findAll() {
        return departmentService.findAll();
    }

    @GetMapping("/title/{title}")
    public Optional<AcademicDepartment> findByTitle(@PathVariable String title) {
        return departmentService.findByTitle(title);
    }

    @GetMapping("/{id}")
    public AcademicDepartment findOne(@PathVariable Integer id) {
        return departmentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicDepartment create(@RequestBody AcademicDepartmentDto department) {
        return departmentService.addOne(department);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        departmentService.findById(id);
        departmentService.deleteById(id);
    }

    @PutMapping("/{id}")
    public AcademicDepartment updateDepartment(@RequestBody AcademicDepartmentDto department, @PathVariable Integer id) {
        return departmentService.update(department, id);
    }

}
