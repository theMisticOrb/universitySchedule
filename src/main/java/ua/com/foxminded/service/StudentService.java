package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;
    private GroupService groupService;
    private StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, GroupService groupService, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.studentMapper = studentMapper;
    }

    public Student addOne(StudentCreateUpdateDto dto) {

        Group group = groupService.findById(dto.getGroupId());
        Student student = studentMapper.toEntity(dto, group);
        try {
            return studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Student with name: " + dto.getLastName() + " " + dto.getFirstName()
                    + " " + dto.getMiddleName() + " alredy exists in this group!");

        }
    }

    public Student findById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    public Student findByFullName(String lastName, String firstName, String middleName) {
        return studentRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found: " + lastName + " " + firstName + " " + middleName));

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
