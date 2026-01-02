package ua.com.foxminded.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.config.UniversityProperties;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.helpers.UpdateUtil;
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
        UpdateUtil.setIfNotNull(updated.getFaculty(), group::setFaculty);
        UpdateUtil.setIfNotNull(updated.getDepartment(), group::setDepartment);
        UpdateUtil.setIfNotNull(updated.getSpecialization(), group::setSpecialization);
        UpdateUtil.setIfNotNull(updated.getStudents(), group::setStudents);
        UpdateUtil.setIfNotNull(updated.getSubjects(), group::setSubjects);
        return group;
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

}
