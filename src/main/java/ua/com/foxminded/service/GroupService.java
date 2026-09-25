package ua.com.foxminded.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.config.UniversityProperties;
import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.StudentRepository;

@Service
@Transactional
public class GroupService implements FullCrudService<Group, GroupCreateUpdateDto, Integer> {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final SpecializationService specializationService;
    private final AcademicDepartmentService departmentService;
    private final TeacherService teacherService;
    private final StudentMapper studentMapper;
    private final SubjectMapper subjectMapper;
    private final UniversityProperties properties;

    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository,
            SpecializationService specializationService, AcademicDepartmentService departmentService,
            TeacherService teacherService, StudentMapper studentMapper, SubjectMapper subjectMapper,
            UniversityProperties properties) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.specializationService = specializationService;
        this.departmentService = departmentService;
        this.teacherService = teacherService;
        this.studentMapper = studentMapper;
        this.subjectMapper = subjectMapper;
        this.properties = properties;
    }

    @Override
    public Group addOne(GroupCreateUpdateDto dto) {
        Group group = new Group();
        Map<Integer, Integer> counter = new HashMap<>();

        Specialization specialization = specializationService.findByCode(dto.getSpecializationCode());

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

            Specialization specialization = specializationService.findByCode(dto.getSpecializationCode());

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

    @Override
    public Group findById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
    }

    public Group findByName(String name) {
        return groupRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Group not found: " + name));
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public Group fullUpdate(GroupCreateUpdateDto updated, Integer groupId) {
        Group group = findById(groupId);

        if (updated.getSpecializationCode() != null) {
            Specialization specialization = specializationService.findByCode(updated.getSpecializationCode());
            UpdateUtil.setIfNotNull(specialization, group::setSpecialization);
        }

        UpdateUtil.replaceSet(group.getStudents(), updated.getStudents(),
                dto -> studentMapper.toEntity(dto, group), studentRepository);

        if (updated.getSubjects() != null) {
            Set<Subject> subjects = updated.getSubjects().stream().map(dto -> {
                List<Teacher> teacher = dto.getTeacherIds().stream().map(teacherService::findById)
                        .collect(Collectors.toList());
                AcademicDepartment department = departmentService.findByCode(dto.getDepartmentCode());
                return subjectMapper.toEntity(dto, teacher, department);
            }).collect(Collectors.toSet());
            group.setSubjects(subjects);
        }

        return group;
    }

    @Override
    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

    private String buildGroupName(Group group, int nextGroupNumber) {

        return group.getSpecialization().getDepartment().getFaculty().getCode() + "."
                + group.getSpecialization().getDepartment().getCode() + "." + group.getSpecialization().getCode() + "."
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
