package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.SubjectDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;

@Component
public class SubjectMapper {

    public Subject toEntity(SubjectDto dto) {
        if (dto == null) {
            return null;
        }

        Subject entity = new Subject();
        Teacher teacher = new Teacher();
        teacher.setId(dto.getTeacherId());

        entity.setTeacher(teacher);
        entity.setTitle(dto.getTitle());
        return entity;

    }
}
