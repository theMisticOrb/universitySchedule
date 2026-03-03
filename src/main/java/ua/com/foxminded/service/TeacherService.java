package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.repository.TeacherRepository;

@Service
@Transactional
public class TeacherService {

    private TeacherRepository teacherRepository;
    private AcademicDepartmentService departmentService;
    private TeacherMapper teacherMapper;
    private SubjectMapper subjectMapper;

    public TeacherService(TeacherRepository teacherRepository, AcademicDepartmentService departmentService,
            TeacherMapper teacherMapper, SubjectMapper subjectMapper) {
        this.teacherRepository = teacherRepository;
        this.departmentService = departmentService;
        this.teacherMapper = teacherMapper;
        this.subjectMapper = subjectMapper;
    }

    public Teacher addOne(TeacherCreateUpdateDto dto) {

        AcademicDepartment department = departmentService.findById(dto.getDepartmentId());
        Teacher teacher = teacherMapper.toEntity(dto, department);

        try {
            return teacherRepository.save(teacher);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Teacher with name: " + dto.getLastName() + " " + dto.getFirstName()
                    + " " + dto.getMiddleName() + " alredy exists in this academic department!");
        }
    }

    public Teacher findById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
    }

    public Teacher findByFullName(String lastName, String firstName, String middleName) {
        return teacherRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Teacher not found: " + lastName + " " + firstName + " " + middleName));

    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher update(TeacherCreateUpdateDto updated, Integer id) {
        Teacher teacher = findById(id);

        if (updated.getDepartmentId() != null) {
            AcademicDepartment department = departmentService.findById(updated.getDepartmentId());
            UpdateUtil.setIfNotNull(department, teacher::setDepartment);
            UpdateUtil.updateListIfNotNull(updated.getSubjects(),
                    dto -> subjectMapper.toEntity(dto, teacher, department));
        }
        UpdateUtil.setIfNotNull(updated.getFirstName(), teacher::setFirstName);
        UpdateUtil.setIfNotNull(updated.getMiddleName(), teacher::setMiddleName);
        UpdateUtil.setIfNotNull(updated.getLastName(), teacher::setLastName);
        return teacher;
    }

    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }

}
