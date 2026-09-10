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
import ua.com.foxminded.repository.TeacherRepository;

@Service
@Transactional
public class SubjectService implements FullCrudService<Subject, SubjectCreateUpdateDto, Integer> {

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final AcademicDepartmentService departmentService;
    private final SpecializationService specializationService;
    private final SubjectMapper subjectMapper;
    private final GroupMapper groupMapper;

    public SubjectService(SubjectRepository subjectRepository, TeacherService teacherService,
            AcademicDepartmentService departmentService, SpecializationService specializationService,
            SubjectMapper subjectMapper, GroupMapper groupMapper, TeacherRepository teacherRepository) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.specializationService = specializationService;
        this.subjectMapper = subjectMapper;
        this.groupMapper = groupMapper;
    }

    @Override
    public Subject addOne(SubjectCreateUpdateDto dto) {

        List<Teacher> teachers = dto.getTeacherEmails().stream().map(teacherService::findByEmail)
                .collect(Collectors.toList());
        AcademicDepartment department = departmentService.findByCode(dto.getDepartmentCode());

        Subject subject = subjectMapper.toEntity(dto, teachers, department);

        try {
            Subject savedSubject = subjectRepository.saveAndFlush(subject);
            teacherRepository.saveAllAndFlush(teachers);
            return savedSubject;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Subject with name: " + dto.getTitle() + " alredy exists!");

        }
    }

    @Override
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

    @Override
    public Subject fullUpdate(SubjectCreateUpdateDto updated, Integer id) {

        Subject subject = findById(id);

        if (updated.getTeacherEmails() != null) {
            List<Teacher> teachers = updated.getTeacherEmails().stream().map(teacherService::findByEmail)
                    .collect(Collectors.toList());
            UpdateUtil.setIfNotNull(teachers, subject::setTeachers);

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

    @Override
    public void deleteById(Integer id) {
        subjectRepository.deleteById(id);
    }

    public void createSubjects(List<SubjectCreateUpdateDto> list) {

        List<Subject> subjects = list.stream().map(this::addOne).collect(Collectors.toList());
        subjectRepository.saveAllAndFlush(subjects);
    }

}
