package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.sdjpa.GroupRepository;

@Service
@Transactional
public class GroupService {

    private GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group create(Group group) {
        return groupRepository.save(group);
    }

    public Group findById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group update(Integer id, Group updated) {
        Group group = findById(id);
        group.setFaculty(updated.getFaculty());
        group.setDepartment(updated.getDepartment());
        group.setSpecialization(updated.getSpecialization());
        group.setStudents(updated.getStudents());
        group.setSubjects(updated.getSubjects());
        return updated;
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }
}
