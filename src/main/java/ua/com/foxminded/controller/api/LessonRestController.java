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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.service.LessonService;

@RestController
@RequestMapping("/api/v1/lessons")
public class LessonRestController {

    private final LessonService service;
    private final LessonMapper mapper;

    public LessonRestController(LessonService service, LessonMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<LessonResponseDto> findAll() {
        return service.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    @GetMapping(params = { "subjectTitle", "weekDay" })
    public List<LessonResponseDto> findBySubjectTitleAndWeekDay(@RequestParam String subjectTitle,
            @RequestParam WeekDay weekDay) {
        return service.findBySubjectTitleAndWeekDay(subjectTitle, weekDay).stream().map(mapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LessonResponseDto findOne(@PathVariable Integer id) {
        return mapper.toResponseDto(service.findById(id));
    }

    @PostMapping("/{teacherId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponseDto create(@RequestBody LessonCreateUpdateDto dto, @PathVariable Integer teacherId) {
        return mapper.toResponseDto(service.addOne(dto, teacherId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.findById(id);
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    public LessonResponseDto update(@RequestBody LessonCreateUpdateDto dto, @PathVariable Integer id) {
        return mapper.toResponseDto(service.update(dto, id));
    }

}
