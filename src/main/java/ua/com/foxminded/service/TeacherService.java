package ua.com.foxminded.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
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
    private PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository, AcademicDepartmentService departmentService,
            TeacherMapper teacherMapper, SubjectMapper subjectMapper, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.departmentService = departmentService;
        this.teacherMapper = teacherMapper;
        this.subjectMapper = subjectMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Teacher addOne(TeacherCreateUpdateDto dto) {
        Teacher teacher = setTeacherValues(dto);
        return teacherRepository.save(teacher);
    }

    public List<Teacher> addAll(List<TeacherCreateUpdateDto> teachersDto) {
        List<Teacher> teachers = new ArrayList<>();

        for (TeacherCreateUpdateDto dto : teachersDto) {
            Teacher teacher = setTeacherValues(dto);
            teachers.add(teacher);
        }
        return teacherRepository.saveAll(teachers);
    }

    public Teacher findById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
    }

    public Teacher findByEmail(String email) {
        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Teacher are not found by this email: " + email));
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

    private Teacher setTeacherValues(TeacherCreateUpdateDto dto) {
        AcademicDepartment department = departmentService.findById(dto.getDepartmentId());
        Teacher teacher = teacherMapper.toEntity(dto, department);

        teacher.setPassword(passwordEncoder.encode(dto.getPassword()));
        String[] roles = teacher.getRoles().stream().map(UserRole::name).toArray(String[]::new);
        if (Arrays.stream(roles).noneMatch("TEACHER"::equals)) {
            throw new IllegalStateException("User must have TEACHER role");
        }
        return teacher;
    }

}
