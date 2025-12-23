package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.sdjpa.SubjectRepository;

@Service
@Transactional
public class SubjectService {

    private SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    
    public Subject create(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject findById(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subject not found: " + id));
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject update(Integer id, Subject updated) {
        Subject subject = findById(id);
        subject.setGroups(updated.getGroups());
        subject.setTeacher(updated.getTeacher());
        subject.setTitle(updated.getTitle());
        return updated;
    }

    public void deleteById(Integer id) {
        subjectRepository.deleteById(id);
    }

}
