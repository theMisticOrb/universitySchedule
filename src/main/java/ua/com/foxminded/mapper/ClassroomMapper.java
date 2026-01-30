package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.dto.classroom.ClassroomResponseDto;
import ua.com.foxminded.entity.Classroom;

@Component
public class ClassroomMapper {

    public Classroom toEntity(ClassroomCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        Classroom entity = new Classroom();
        entity.setName(dto.getName());
        return entity;
    }

    public ClassroomResponseDto toDto(Classroom entity) {
        if (entity == null) {
            return null;
        }

        ClassroomResponseDto dto = new ClassroomResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
