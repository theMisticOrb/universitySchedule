package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Student;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.sdjpa.StudentRepository;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addOne(Student student) {
        return studentRepository.save(student);
    }

    public Student findById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found: " + id));
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student update(Student updated) {
        Student student = findById(updated.getId());
        UpdateUtil.setIfNotNull(updated.getFirstName(), student::setFirstName);
        UpdateUtil.setIfNotNull(updated.getMiddleName(), student::setMiddleName);
        UpdateUtil.setIfNotNull(updated.getLastName(), student::setLastName);
        UpdateUtil.setIfNotNull(updated.getGroup(), student::setGroup);
        return student;
    }

    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

}
