package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.specialization.SpecializationResponseDto;
import ua.com.foxminded.entity.Specialization;

@Component
public class SpecializationMapper {

    public Specialization toEntity(SpecializationCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        Specialization entity = new Specialization();
        entity.setTitle(dto.getTitle());
        return entity;
    }
    
    public SpecializationResponseDto toDto(Specialization entity) {
        if(entity==null) {
            return null;
        }
        
        SpecializationResponseDto dto = new SpecializationResponseDto();
        dto.setId(entity.getId());
        dto.setDepartmentId(entity.getDepartment().getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }

   

}
