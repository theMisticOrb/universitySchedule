package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;

@Component
public class LessonMapper {

    public Lesson toEntity(LessonCreateUpdateDto dto) {
        Objects.requireNonNull(dto, "LessonCreateUpdateDto must not be null!");

        Lesson entity = new Lesson();

        Classroom classroom = new Classroom();
        classroom.setId(dto.getClassroomId());

        Subject subject = new Subject();
        subject.setId(dto.getSubjectId());

        entity.setClassroom(classroom);
        entity.setClassHour(dto.getClassHour());
        entity.setWeekDay(dto.getWeekDay());
        entity.setSubject(subject);
        return entity;
    }

    public LessonResponseDto toResponseDto(Lesson entity) {
        Objects.requireNonNull(entity, "Lesson must not be null!");

        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(entity.getId());
        dto.setSubjectTitle(entity.getSubject().getTitle());
        dto.setClassHour(entity.getClassHour());
        dto.setClassroomName(entity.getClassroom().getName());
        dto.setWeekDay(entity.getWeekDay());
        return dto;
    }

}
