package ua.com.foxminded.controller.api;

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
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.service.AcademicDepartmentService;

@RestController
@RequestMapping("/api/departments")
public class AcademicDepartmentController {

    private final AcademicDepartmentService service;

    public AcademicDepartmentController(AcademicDepartmentService departmentService) {
        this.service = departmentService;
    }

    @GetMapping
    public Iterable<AcademicDepartment> findAll() {
        return service.findAll();
    }

    @GetMapping("/title/{title}")
    public AcademicDepartment findByTitle(@PathVariable String title) {
        return service.findByTitle(title);
    }

    @GetMapping("/{id}")
    public AcademicDepartment findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicDepartment create(@RequestBody AcademicDepartmentCreateUpdateDto dto) {
        return service.addOne(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public AcademicDepartment update(@RequestBody AcademicDepartmentCreateUpdateDto dto, @PathVariable Integer id) {
        return service.update(dto, id);
    }

}
