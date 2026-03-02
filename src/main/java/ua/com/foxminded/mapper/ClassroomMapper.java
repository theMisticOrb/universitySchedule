package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.dto.classroom.ClassroomResponseDto;
import ua.com.foxminded.entity.Classroom;

@Component
public class ClassroomMapper {

    public Classroom toEntity(ClassroomCreateUpdateDto dto) {
        Objects.requireNonNull(dto, "ClassroomCreateUpdateDto must not be null!");

        Classroom entity = new Classroom();
        entity.setName(dto.getName());
        return entity;
    }

    public ClassroomResponseDto toResponseDto(Classroom entity) {
        Objects.requireNonNull(entity, "Classroom must not be null!");

        ClassroomResponseDto dto = new ClassroomResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

}
