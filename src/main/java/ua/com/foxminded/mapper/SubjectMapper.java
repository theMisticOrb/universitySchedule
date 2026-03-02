package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;

@Component
public class SubjectMapper {

    public Subject toEntity(SubjectCreateUpdateDto dto) {
        Objects.requireNonNull(dto, "SubjectCreateUpdateDto must not be null!");

        Subject entity = new Subject();
        Teacher teacher = new Teacher();
        AcademicDepartment department = new AcademicDepartment();
        teacher.setId(dto.getTeacherId());
        department.setId(dto.getDepartmentId());

        entity.setTeacher(teacher);
        entity.setTitle(dto.getTitle());
        entity.setDepartment(department);
        return entity;

    }

    public SubjectResponseDto toResponseDto(Subject entity) {
        Objects.requireNonNull(entity, "Subject must not be null!");

        SubjectResponseDto dto = new SubjectResponseDto();
        dto.setId(entity.getId());
        dto.setTeacherId(entity.getTeacher().getId());
        dto.setTitle(entity.getTitle());
        dto.setDepartmentId(entity.getDepartment().getId());
        return dto;
    }
}
