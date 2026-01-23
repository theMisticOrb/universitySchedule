package ua.com.foxminded.mapper;

import ua.com.foxminded.dto.LessonDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;

public class LessonMapper {

    public Lesson toEntity(LessonDto dto) {
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

}
