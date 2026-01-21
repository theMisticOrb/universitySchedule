package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.AcademicDepartmentDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;

@Component
public class AcademicDepartmentMapper {

    public AcademicDepartment toEntity(AcademicDepartmentDto dto) {
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

}
