package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.GroupService;

@Component
public class GroupGenerator {
    private final GroupService service;

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
            dto.setDepartmentId(specialization.getId());

            try {
                Group group = service.addOne(dto);
                groups.add(group);
            } catch (DuplicateEntityException e) {
                e.printStackTrace();
            }

        }

        return groups;
    }

}
