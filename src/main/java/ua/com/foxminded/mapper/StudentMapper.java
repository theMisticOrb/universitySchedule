package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;

@Component
public class StudentMapper {
    
    public Student toEntity(StudentCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        Student entity = new Student();
        Group group = new Group();
        group.setId(dto.getGroupId());
        
        entity.setFirstName(dto.getFirstName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLastName(dto.getLastName());
        entity.setGroup(group);
        return entity;
    }
    
    public StudentResponseDto toDto(Student entity) {
        if(entity==null) {
            return null;
        }
        
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setLastName(entity.getLastName());
        dto.setGroupId(entity.getGroup().getId());
        return dto;
    }
}
