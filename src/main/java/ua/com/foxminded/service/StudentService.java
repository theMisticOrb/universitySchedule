package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Student;
import ua.com.foxminded.helpers.UpdateUtils;
import ua.com.foxminded.sdjpa.StudentRepository;

@Service
@Transactional
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(Student student) {
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
        UpdateUtils.setIfNotNull(updated.getFirstName(), student::setFirstName);
        UpdateUtils.setIfNotNull(updated.getMiddleName(), student::setMiddleName);
        UpdateUtils.setIfNotNull(updated.getLastName(), student::setLastName);
        UpdateUtils.setIfNotNull(updated.getGroup(), student::setGroup);
        return updated;
    }

    public void deleteById(Integer id) {
        studentRepository.deleteById(id);
    }

}
