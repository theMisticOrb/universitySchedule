package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.specialization.SpecializationResponseDto;
import ua.com.foxminded.entity.Specialization;

@Component
public class SpecializationMapper {

    public Specialization toEntity(SpecializationCreateUpdateDto dto) {
        Objects.requireNonNull(dto, "SpecializationCreateUpdateDto must not be null!");

        Specialization entity = new Specialization();
        entity.setTitle(dto.getTitle());
        return entity;
    }

    public SpecializationResponseDto toResponseDto(Specialization entity) {
        Objects.requireNonNull(entity, "Specialization must not be null!");

        SpecializationResponseDto dto = new SpecializationResponseDto();
        dto.setId(entity.getId());
        dto.setDepartmentId(entity.getDepartment().getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }

}
