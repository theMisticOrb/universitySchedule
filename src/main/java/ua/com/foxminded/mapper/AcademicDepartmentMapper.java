package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.dto.department.AcademicDepartmentResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;

@Component
public class AcademicDepartmentMapper {

    public AcademicDepartment toEntity(AcademicDepartmentCreateUpdateDto dto, Faculty faculty) {
        Objects.requireNonNull(dto, "AcademicDepartmentCreateUpdateDto must not be null!");
        Objects.requireNonNull(faculty, "Faculty must not be null!");

        AcademicDepartment entity = new AcademicDepartment();

        entity.setTitle(dto.getTitle());
        entity.setCode(dto.getCode());
        entity.setFaculty(faculty);
        return entity;
    }

    public AcademicDepartmentResponseDto toResponseDto(AcademicDepartment entity) {
        Objects.requireNonNull(entity, "AcademicDepartment must not be null!");

        AcademicDepartmentResponseDto dto = new AcademicDepartmentResponseDto();
        dto.setId(entity.getId());
        dto.setFacultyCode(entity.getFaculty().getCode());
        dto.setTitle(entity.getTitle());
        dto.setCode(entity.getCode());
        return dto;
    }

}
