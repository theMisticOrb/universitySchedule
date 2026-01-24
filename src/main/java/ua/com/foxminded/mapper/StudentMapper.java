package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.StudentDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;

@Component
public class StudentMapper {
    
    public Student toEntity(StudentDto dto) {
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
}
