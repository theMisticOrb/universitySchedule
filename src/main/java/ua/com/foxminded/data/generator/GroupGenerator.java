package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.sdjpa.GroupRepository;

@Component
@Profile("dev")
public class GroupGenerator {
    private final GroupRepository repository;

    public GroupGenerator(GroupRepository repository) {
        this.repository = repository;
    }

    public List<Group> generateGroup(int specializationCount, Specialization specialization) {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < specializationCount; i++) {

            Group group = new Group();
            group.setFaculty(specialization.getDepartment().getFaculty());
            group.setDepartment(specialization.getDepartment());
            group.setSpecialization(specialization);

            groups.add(group);

        }
        repository.saveAll(groups);

        return groups;
    }

}
