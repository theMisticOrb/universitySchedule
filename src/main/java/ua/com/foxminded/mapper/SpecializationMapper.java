package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.SpecializationDto;
import ua.com.foxminded.entity.Specialization;

@Component
public class SpecializationMapper {

    public Specialization toEntity(SpecializationDto dto) {
        if (dto == null) {
            return null;
        }

        Specialization entity = new Specialization();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        return entity;
    }

   

}
