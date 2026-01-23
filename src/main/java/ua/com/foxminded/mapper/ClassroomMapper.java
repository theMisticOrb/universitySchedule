package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.ClassroomDto;
import ua.com.foxminded.entity.Classroom;

@Component
public class ClassroomMapper {

    public Classroom toEntity(ClassroomDto dto) {
        if (dto == null) {
            return null;
        }

        Classroom entity = new Classroom();
        entity.setName(dto.getName());
        return entity;
    }

}
