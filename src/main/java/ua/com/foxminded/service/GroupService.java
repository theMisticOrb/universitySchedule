package ua.com.foxminded.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.config.UniversityProperties;
import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.repository.GroupRepository;

@Service
@Transactional
public class GroupService {

    private GroupRepository groupRepository;
    private SpecializationService specializationService;

    private UniversityProperties properties;

    public GroupService(GroupRepository groupRepository, SpecializationService specializationService,
            UniversityProperties properties) {
        this.groupRepository = groupRepository;
        this.specializationService = specializationService;
        this.properties = properties;
    }

    public Group addOne(GroupCreateUpdateDto dto) {
        Group group = new Group();
        Map<Integer, Integer> counter = new HashMap<>();

        Specialization specialization = specializationService.findById(dto.getSpecializationId());

        int nextGroupNumber = counter.compute(specialization.getId(), (specializationId, currentValue) -> {
            if (currentValue == null) {
                return groupRepository.countBySpecialization_Id(specializationId) + 1;
            }
            return currentValue + 1;
        });

        setGroupValues(specialization, group, nextGroupNumber);

        return groupRepository.save(group);
    }

    public List<Group> addAll(List<GroupCreateUpdateDto> groupsDto) {
        List<Group> groups = new ArrayList<>();
        Map<Integer, Integer> counter = new HashMap<>();
        for (GroupCreateUpdateDto dto : groupsDto) {

            Group group = new Group();

            Specialization specialization = specializationService.findById(dto.getSpecializationId());

            int nextGroupNumber = counter.compute(specialization.getId(), (specializationId, currentValue) -> {
                if (currentValue == null) {
                    return groupRepository.countBySpecialization_Id(specializationId) + 1;
                }
                return currentValue + 1;
            });

            setGroupValues(specialization, group, nextGroupNumber);

            groups.add(group);

        }

        return groupRepository.saveAll(groups);

    }

    public Group findById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
    }

    public Group findByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + name));
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group update(GroupCreateUpdateDto updated, Integer id) {
        Group group = findById(id);

        StudentMapper studentMapper = new StudentMapper();
        SubjectMapper subjectMapper = new SubjectMapper();

        if (updated.getSpecializationId() != null) {
            Specialization specialization = specializationService.findById(updated.getSpecializationId());
            UpdateUtil.setIfNotNull(specialization, group::setSpecialization);
        }

        UpdateUtil.updateListIfNotNull(updated.getStudents(), studentMapper::toEntity);
        UpdateUtil.updateListIfNotNull(updated.getSubjects(), subjectMapper::toEntity);
        return group;
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

    private String buildGroupName(Group group, int nextGroupNumber) {

        return group.getSpecialization().getDepartment().getFaculty().getId() + "."
                + group.getSpecialization().getDepartment().getId() + "." + group.getSpecialization().getId() + "."
                + nextGroupNumber + "." + group.getYearOfUniv().getCurentYearInUniv();
    }

    private void setGroupValues(Specialization specialization, Group group, int nextGroupNumber) {

        group.setSpecialization(specialization);
        group.setStartDate(LocalDate.now());
        int maxYears = properties.getTotalYearsOfStudy();
        group.setYearOfUniv(new YearOfUniv(group.getStartDate(), maxYears));
        String groupName = buildGroupName(group, nextGroupNumber);
        group.setName(groupName);

        if (groupRepository.existsBySpecialization_IdAndName(specialization.getId(), group.getName())) {
            throw new DuplicateEntityException("Group with name: " + group.getName() + " alredy exists!");
        }
    }

}
