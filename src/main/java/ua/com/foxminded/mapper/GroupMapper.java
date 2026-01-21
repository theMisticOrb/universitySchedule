package ua.com.foxminded.mapper;

import ua.com.foxminded.dto.AcademicDepartmentDto;
import ua.com.foxminded.dto.GroupDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;

public class GroupMapper {

    public Group toEntity(GroupDto dto) {
        if (dto == null) {
            return null;
        }

        Group entity = new Group();

        Faculty faculty = new Faculty();
        faculty.setId(dto.getFacultyId());

        AcademicDepartment department = new AcademicDepartment();
        department.setId(dto.getDepartmentId());
        
        Specialization specialization = new Specialization();
        specialization.setId(dto.getSpecializationId());

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setFaculty(faculty);
        entity.setDepartment(department);
        entity.setSpecialization(specialization);
        entity.setStartDate(dto.getStartDate());
        return entity;
    }

    public AcademicDepartmentDto toDto(AcademicDepartment entity) {
        if (entity == null) {
            return null;
        }

        AcademicDepartmentDto dto = new AcademicDepartmentDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setFacultyId(entity.getFaculty().getId());
        return dto;
    }

}
