package ua.com.foxminded.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.config.UniversityProperties;
import ua.com.foxminded.dto.groupdto.GroupCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.sdjpa.GroupRepository;

@Service
@Transactional
public class GroupService {

    private GroupRepository groupRepository;
    private FacultyService facultyService;
    private AcademicDepartmentService departmentService;
    private SpecializationService specializationService;

    private UniversityProperties properties;

    public GroupService(GroupRepository groupRepository, FacultyService facultyService,
            AcademicDepartmentService departmentService, SpecializationService specializationService,
            UniversityProperties properties) {
        this.groupRepository = groupRepository;
        this.facultyService = facultyService;
        this.departmentService = departmentService;
        this.specializationService = specializationService;
        this.properties = properties;
    }

    public Group addOne(GroupCreateUpdateDto dto) {
        Group group = new Group();

        Faculty faculty = facultyService.findById(dto.getFacultyId());
        AcademicDepartment department = departmentService.findById(dto.getDepartmentId());
        Specialization specialization = specializationService.findById(dto.getSpecializationId());

        group.setFaculty(faculty);
        group.setDepartment(department);
        group.setSpecialization(specialization);
        group.setStartDate(LocalDate.now());
        int maxYears = properties.getTotalYearsOfStudy();
        group.setYearOfUniv(new YearOfUniv(group.getStartDate(), maxYears));
        group.setName(group.getName());
        return groupRepository.save(group);
    }

    public Group findById(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Group not found: " + id));
    }

    public Optional<Group> findByTitle(String title) {
        return groupRepository.findByName(title);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group update(GroupCreateUpdateDto updated, Integer id) {
        Group group = findById(id);

        StudentMapper studentMapper = new StudentMapper();
        SubjectMapper subjectMapper = new SubjectMapper();

        if (updated.getFacultyId() != null) {
            Faculty faculty = facultyService.findById(updated.getFacultyId());
            UpdateUtil.setIfNotNull(faculty, group::setFaculty);
        }
        if (updated.getDepartmentId() != null) {
            AcademicDepartment department = departmentService.findById(updated.getDepartmentId());
            UpdateUtil.setIfNotNull(department, group::setDepartment);
        }
        if (updated.getSpecializationId() != null) {
            Specialization specialization = specializationService.findById(updated.getSpecializationId());
            UpdateUtil.setIfNotNull(specialization, group::setSpecialization);
        }

        UpdateUtil.updateListIfNotNull(updated.getStudents(), group.getStudents(), studentMapper::toEntity);
        UpdateUtil.updateListIfNotNull(updated.getSubjects(), group.getSubjects(), subjectMapper::toEntity);
        return group;
    }

    public void deleteById(Integer id) {
        groupRepository.deleteById(id);
    }

}
