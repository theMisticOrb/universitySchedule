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

import ua.com.foxminded.dto.groupdto.GroupCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.service.GroupService;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    
    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }
    
    @GetMapping
    public Iterable<Group> findAll() {
        return service.findAll();
    }

    @GetMapping("/title/{title}")
    public Optional<Group> findByTitle(@PathVariable String title) {
        return service.findByTitle(title);
    }

    @GetMapping("/{id}")
    public Group findOne(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Group create(@RequestBody GroupCreateUpdateDto dto) {
        return service.addOne(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public Group update(@RequestBody GroupCreateUpdateDto dto, @PathVariable Integer id) {
        return service.update(dto, id);
    }

}
