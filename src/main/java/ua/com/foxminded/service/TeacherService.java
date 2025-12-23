package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.sdjpa.TeacherRepository;

@Service
@Transactional
public class TeacherService {

    private TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher findById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher update(Integer id, Teacher updated) {
        Teacher teacher = findById(id);
        teacher.setDepartment(updated.getDepartment());
        teacher.setFirstName(updated.getFirstName());
        teacher.setMiddleName(updated.getMiddleName());
        teacher.setLastName(updated.getLastName());
        return updated;
    }

    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }

}
