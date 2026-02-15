package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.dto.user.TeacherResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        Teacher entity = new Teacher();
        AcademicDepartment department = new AcademicDepartment();
        department.setId(dto.getDepartmentId());

        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setDepartment(department);
        return entity;
    }

    public TeacherResponseDto toResponseDto(Teacher entity) {
        if (entity == null) {
            return null;
        }

        TeacherResponseDto dto = new TeacherResponseDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setDepartmentId(entity.getDepartment().getId());
        return dto;
    }
}
