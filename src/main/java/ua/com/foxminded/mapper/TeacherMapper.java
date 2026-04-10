package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.dto.user.TeacherResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherCreateUpdateDto dto, AcademicDepartment department) {
        Objects.requireNonNull(dto, "TeacherCreateUpdateDto must not be null!");
        Objects.requireNonNull(department, "AcademicDepartment must not be null!");

        Teacher entity = new Teacher();

        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setDepartment(department);
        entity.setRoles(dto.getRoles());
        entity.setEmail(dto.getEmail());
        return entity;
    }

    public TeacherResponseDto toResponseDto(Teacher entity) {
        Objects.requireNonNull(entity, "Teacher must not be null!");

        TeacherResponseDto dto = new TeacherResponseDto();
        dto.setId(entity.getId());
        dto.setUserRoles(entity.getRoles());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setDepartmentId(entity.getDepartment().getId());
        dto.setDepartmentCode(entity.getDepartment().getCode());
        dto.setEmail(entity.getEmail());
        return dto;
    }
}
