package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.sdjpa.StudentRepository;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;
    private GroupService groupService;

    public StudentService(StudentRepository studentRepository, GroupService groupService) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
    }

    public Student addOne(StudentCreateUpdateDto dto) {
        Student student = new Student();
        Group group = groupService.findById(dto.getGroupId());

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setMiddleName(dto.getMiddleName());
        student.setGroup(group);
        return studentRepository.save(student);
    }

    public Student findById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    public Optional<Student> findByFullName(String lastName, String firstName, String middleName) {
        return studentRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName);

    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student update(StudentCreateUpdateDto updated, Integer id) {

        Student student = findById(id);

        if (updated.getGroupId() != null) {

            Group group = groupService.findById(updated.getGroupId());
            UpdateUtil.setIfNotNull(group, student::setGroup);

        }

        UpdateUtil.setIfNotNull(updated.getFirstName(), student::setFirstName);
        UpdateUtil.setIfNotNull(updated.getMiddleName(), student::setMiddleName);
        UpdateUtil.setIfNotNull(updated.getLastName(), student::setLastName);
        return student;
    }

    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

}
