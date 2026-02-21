package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.service.GroupService;

@Component
@Profile("dev")
public class GroupGenerator {
    private final GroupService service;

    public GroupGenerator(GroupService service) {
        this.service = service;
    }

    public List<Group> generateGroup(int groupCount, Specialization specialization) {
        List<GroupCreateUpdateDto> groups = new ArrayList<>();

        for (int i = 0; i < groupCount; i++) {

            GroupCreateUpdateDto group = new GroupCreateUpdateDto();
            group.setSpecializationId(specialization.getId());

            groups.add(group);

        }
        return service.addAll(groups);
    }

}
