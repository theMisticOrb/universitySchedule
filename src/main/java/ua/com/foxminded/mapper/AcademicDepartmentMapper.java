package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.dto.department.AcademicDepartmentResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;

@Component
public class AcademicDepartmentMapper {

    public AcademicDepartment toEntity(AcademicDepartmentCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        AcademicDepartment entity = new AcademicDepartment();

        Faculty faculty = new Faculty();
        faculty.setId(dto.getFacultyId());

        entity.setTitle(dto.getTitle());
        entity.setFaculty(faculty);
        return entity;
    }

    public AcademicDepartmentResponseDto toResponseDto(AcademicDepartment entity) {
        if (entity == null) {
            return null;
        }

        AcademicDepartmentResponseDto dto = new AcademicDepartmentResponseDto();
        dto.setId(entity.getId());
        dto.setFacultyId(entity.getFaculty().getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }

}
