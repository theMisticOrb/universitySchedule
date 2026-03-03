package ua.com.foxminded.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.group.GroupResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;

@Component
public class GroupMapper {

    public Group toEntity(GroupCreateUpdateDto dto, Specialization specialization) {
        Objects.requireNonNull(dto, "GroupCreateUpdateDto must not be null!");
        Objects.requireNonNull(specialization, "Specialization must not be null!");

        Group entity = new Group();

        entity.setSpecialization(specialization);
        return entity;
    }

    public GroupResponseDto toResponseDto(Group entity) {
        Objects.requireNonNull(entity, "Group must not be null!");

        GroupResponseDto dto = new GroupResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecializationId(entity.getSpecialization().getId());
        dto.setStartDate(entity.getStartDate());
        dto.setCurrentYearOfUniv(entity.getYearOfUniv().getCurentYearInUniv());
        return dto;
    }

}
