package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.sdjpa.SubjectRepository;

@Service
@Transactional
public class SubjectService {

    private SubjectRepository subjectRepository;
    private TeacherService teacherService;

    public SubjectService(SubjectRepository subjectRepository, TeacherService teacherService) {
        this.subjectRepository = subjectRepository;
        this.teacherService = teacherService;
    }

    public Subject addOne(SubjectCreateUpdateDto dto) {
        Subject subject = new Subject();
        Teacher teacher = teacherService.findById(dto.getTeacherId());

        subject.setTeacher(teacher);
        subject.setTitle(dto.getTitle());
        return subjectRepository.save(subject);
    }

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

    public Subject update(SubjectCreateUpdateDto updated, Integer id) {

        Subject subject = findById(id);
        GroupMapper mapper = new GroupMapper();

        if (updated.getTeacherId() != null) {
            Teacher teacher = teacherService.findById(updated.getTeacherId());
            UpdateUtil.setIfNotNull(teacher, subject::setTeacher);

        }

        UpdateUtil.updateListIfNotNull(updated.getGroups(), subject.getGroups(), mapper::toEntity);
        UpdateUtil.setIfNotNull(updated.getTitle(), subject::setTitle);
        return subject;
    }

    public void deleteById(Integer id) {
        subjectRepository.deleteById(id);
    }

}
