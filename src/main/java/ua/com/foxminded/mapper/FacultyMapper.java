package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.dto.faculty.FacultyResponseDto;
import ua.com.foxminded.entity.Faculty;

@Component
public class FacultyMapper {

    public Faculty toEntity(FacultyCreateUpdateDto dto) {
        Objects.requireNonNull(dto, "FacultyCreateUpdateDto must not be null!");

        Faculty entity = new Faculty();
        entity.setTitle(dto.getTitle());
        entity.setCode(dto.getCode());
        return entity;
    }

    public FacultyResponseDto toResponseDto(Faculty entity) {
        Objects.requireNonNull(entity, "Faculty must not be null!");

        FacultyResponseDto dto = new FacultyResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCode(entity.getCode());
        return dto;
    }

}