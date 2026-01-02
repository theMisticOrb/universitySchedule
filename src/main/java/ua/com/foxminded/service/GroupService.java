package ua.com.foxminded.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.config.UniversityProperties;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.helpers.UpdateUtils;
import ua.com.foxminded.sdjpa.GroupRepository;

@Service
@Transactional
public class GroupService {

    private GroupRepository groupRepository;

    private UniversityProperties properties;

    public GroupService(GroupRepository groupRepository, UniversityProperties properties) {
        this.groupRepository = groupRepository;
        this.properties = properties;
    }

    public Group addOne(Group group) {
        group.setStartDate(LocalDate.now());
        int maxYears = properties.getTotalYearsOfStudy();
        group.setYearOfUniv(new YearOfUniv(group.getStartDate(), maxYears));
        return groupRepository.save(group);
    }

    public Group findById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group update(Group updated) {
        Group group = findById(updated.getId());
        UpdateUtils.setIfNotNull(updated.getFaculty(), group::setFaculty);
        UpdateUtils.setIfNotNull(updated.getDepartment(), group::setDepartment);
        UpdateUtils.setIfNotNull(updated.getSpecialization(), group::setSpecialization);
        UpdateUtils.setIfNotNull(updated.getStudents(), group::setStudents);
        UpdateUtils.setIfNotNull(updated.getSubjects(), group::setSubjects);
        return updated;
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

}
