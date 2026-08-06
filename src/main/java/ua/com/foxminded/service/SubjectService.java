package ua.com.foxminded.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.repository.SubjectRepository;

@Service
@Transactional
public class SubjectService {

    private SubjectRepository subjectRepository;
    private TeacherService teacherService;
    private AcademicDepartmentService departmentService;
    private SpecializationService specializationService;
    private SubjectMapper subjectMapper;
    private GroupMapper groupMapper;

    public SubjectService(SubjectRepository subjectRepository, TeacherService teacherService,
            AcademicDepartmentService departmentService, SpecializationService specializationService,
            SubjectMapper subjectMapper, GroupMapper groupMapper) {
        this.subjectRepository = subjectRepository;
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.specializationService = specializationService;
        this.subjectMapper = subjectMapper;
        this.groupMapper = groupMapper;
    }

    public Subject addOne(SubjectCreateUpdateDto dto) {

        Teacher teacher = teacherService.findByEmail(dto.getTeacherEmail());
        AcademicDepartment department = departmentService.findByCode(dto.getDepartmentCode());

        Subject subject = subjectMapper.toEntity(dto, teacher, department);

        try {
            return subjectRepository.save(subject);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Subject with name: " + dto.getTitle() + " alredy exists!");

        }
    }

    public Subject findById(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found: " + id));
    }

    public Subject findByTitle(String title) {
        return subjectRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found: " + title));
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject update(SubjectCreateUpdateDto updated, Integer id) {

        Subject subject = findById(id);

        if (updated.getTeacherEmail() != null) {
            Teacher teacher = teacherService.findByEmail(updated.getTeacherEmail());
            UpdateUtil.setIfNotNull(teacher, subject::setTeacher);

        }
        UpdateUtil.setIfNotNull(updated.getTitle(), subject::setTitle);

        if (updated.getGroups() != null) {
            Set<Group> groups = updated.getGroups().stream().map(dto -> {
                Specialization specialization = specializationService.findByCode(dto.getSpecializationCode());
                return groupMapper.toEntity(dto, specialization);
            }).collect(Collectors.toSet());

            subject.setGroups(groups);
        }

        return subject;
    }

    public void deleteById(Integer id) {
        subjectRepository.deleteById(id);
    }

}
