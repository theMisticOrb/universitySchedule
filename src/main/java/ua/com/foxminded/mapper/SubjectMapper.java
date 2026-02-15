package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectResponseDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;

@Component
public class SubjectMapper {

    public Subject toEntity(SubjectCreateUpdateDto dto) {
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

    public SubjectResponseDto toResponseDto(Subject entity) {
        if (entity == null) {
            return null;
        }

        SubjectResponseDto dto = new SubjectResponseDto();
        dto.setId(entity.getId());
        dto.setTeacherId(entity.getTeacher().getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }
}
