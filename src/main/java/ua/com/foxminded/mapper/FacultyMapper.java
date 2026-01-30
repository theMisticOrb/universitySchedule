package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.dto.faculty.FacultyResponseDto;
import ua.com.foxminded.entity.Faculty;

@Component
public class FacultyMapper {

    public Faculty toEntity(FacultyCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        Faculty entity = new Faculty();
        entity.setTitle(dto.getTitle());
        return entity;
    }
    
    public FacultyResponseDto toDto(Faculty entity) {
        if(entity==null) {
            return null;
        }
        
        FacultyResponseDto dto = new FacultyResponseDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }

}
