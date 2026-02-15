package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.GroupService;

@Component
@Profile("dev")
public class GroupGenerator {
    private final GroupService service;
    private final Logger log = LoggerFactory.getLogger(GroupGenerator.class);

    public GroupGenerator(GroupService service) {
        this.service = service;
    }

    public List<Group> generateGroup(int specializationCount, Specialization specialization) {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < specializationCount; i++) {

            GroupCreateUpdateDto dto = new GroupCreateUpdateDto();
            dto.setFacultyId(specialization.getDepartment().getFaculty().getId());
            dto.setDepartmentId(specialization.getDepartment().getId());
            dto.setSpecializationId(specialization.getId());

            try {
                Group group = service.addOne(dto);
                groups.add(group);
            } catch (DuplicateEntityException e) {
                log.warn("Duplicate group entity!");
            }

        }

        return groups;
    }

}
