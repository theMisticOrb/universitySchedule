package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.sdjpa.TeacherRepository;

@Service
@Transactional
public class TeacherService {

    private TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher addOne(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public Teacher findById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher update(Teacher updated) {
        Teacher teacher = findById(updated.getId());
        UpdateUtil.setIfNotNull(updated.getDepartment(), teacher::setDepartment);
        UpdateUtil.setIfNotNull(updated.getFirstName(), teacher::setFirstName);
        UpdateUtil.setIfNotNull(updated.getMiddleName(), teacher::setMiddleName);
        UpdateUtil.setIfNotNull(updated.getLastName(), teacher::setLastName);
        UpdateUtil.setIfNotNull(updated.getSubjects(), teacher::setSubjects);
        return teacher;
    }

    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }

}
