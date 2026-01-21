package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.FacultyDto;
import ua.com.foxminded.entity.Faculty;

@Component
public class FacultyMapper {

    public Faculty toEntity(FacultyDto dto) {
        if (dto == null) {
            return null;
        }

        Faculty entity = new Faculty();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        return entity;
    }

    public FacultyDto toDto(Faculty entity) {
        if (entity == null) {
            return null;
        }
        
        FacultyDto dto = new FacultyDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }
}
