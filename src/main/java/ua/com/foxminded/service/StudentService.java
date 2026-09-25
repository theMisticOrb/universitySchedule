package ua.com.foxminded.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.repository.StudentRepository;

@Service
@Transactional
public class StudentService implements FullCrudService<Student, StudentCreateUpdateDto, Integer> {

    private final StudentRepository studentRepository;
    private final GroupService groupService;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, GroupService groupService, StudentMapper studentMapper,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Student addOne(StudentCreateUpdateDto dto) {
        Student student = setStudentValues(dto);
        return studentRepository.save(student);
    }

    public List<Student> addAll(List<StudentCreateUpdateDto> studentsDto) {
        List<Student> students = new ArrayList<>();

        for (StudentCreateUpdateDto dto : studentsDto) {

            Student student = setStudentValues(dto);
            students.add(student);
        }

        return studentRepository.saveAll(students);

    }

    @Override
    public Student findById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    public Student findByFullName(String lastName, String firstName, String middleName) {
        return studentRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found: " + lastName + " " + firstName + " " + middleName));

    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student are not found by this email: " + email));

    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student fullUpdate(StudentCreateUpdateDto updated, Integer id) {

        Student student = findById(id);

        if (updated.getGroupName() != null) {
            Group group = groupService.findByName(updated.getGroupName());
            UpdateUtil.setIfNotNull(group, student::setGroup);
        }

        UpdateUtil.setIfNotNull(updated.getFirstName(), student::setFirstName);
        UpdateUtil.setIfNotNull(updated.getMiddleName(), student::setMiddleName);
        UpdateUtil.setIfNotNull(updated.getLastName(), student::setLastName);
        return student;
    }

    @Override
    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

    private Student setStudentValues(StudentCreateUpdateDto dto) {
        Group group = groupService.findByName(dto.getGroupName());
        Student student = studentMapper.toEntity(dto, group);

        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        String[] roles = student.getRoles().stream().map(UserRole::name).toArray(String[]::new);

        if (Arrays.stream(roles).noneMatch("STUDENT"::equals)) {
            throw new IllegalStateException("User must have STUDENT role");
        }
        return student;
    }

    public void changeStudentGroup(Integer id, Integer newGroupId) {
        Student student = findById(id);
        Group newGroup = groupService.findById(newGroupId);
        UpdateUtil.setIfNotNull(newGroup, student::setGroup);
    }

    public long count() {
        return studentRepository.count();
    }

}
