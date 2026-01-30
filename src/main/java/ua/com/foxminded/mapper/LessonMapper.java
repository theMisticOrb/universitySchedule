package ua.com.foxminded.mapper;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;

public class LessonMapper {

    public Lesson toEntity(LessonCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

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

    public LessonResponseDto toDto(Lesson entity) {
        if (entity == null) {
            return null;
        }

        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(entity.getId());
        dto.setSubjectId(entity.getSubject().getId());
        dto.setClassHour(entity.getClassHour());
        dto.setClassroomId(entity.getClassroom().getId());
        dto.setWeekDay(entity.getWeekDay());
        return dto;
    }

}
