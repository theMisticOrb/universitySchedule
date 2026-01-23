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
        entity.setTitle(dto.getTitle());
        return entity;
    }

}
