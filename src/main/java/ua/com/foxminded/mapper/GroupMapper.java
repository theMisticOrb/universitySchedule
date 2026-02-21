package ua.com.foxminded.mapper;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.group.GroupResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;

@Component
public class GroupMapper {

    public Group toEntity(GroupCreateUpdateDto dto) {
        if (dto == null) {
            return null;
        }

        Group entity = new Group();

        Specialization specialization = new Specialization();
        specialization.setId(dto.getSpecializationId());

        entity.setSpecialization(specialization);
        return entity;
    }

    public GroupResponseDto toResponseDto(Group entity) {
        if (entity == null) {
            return null;
        }

        GroupResponseDto dto = new GroupResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecializationId(entity.getSpecialization().getId());
        dto.setStartDate(entity.getStartDate());
        dto.setCurrentYearOfUniv(entity.getYearOfUniv().getCurentYearInUniv());
        return dto;
    }

}
