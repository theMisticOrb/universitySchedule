package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.sdjpa.LessonRepository;

@Service
@Transactional
public class LessonService {

    private LessonRepository lessonRepository;

    protected LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }
    
    public Lesson create(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public Lesson findById(Integer id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson update(Integer id, Lesson updated) {
        Lesson lesson = findById(id);
        lesson.setClassHours(updated.getClassHours());
        lesson.setClassroom(updated.getClassroom());
        lesson.setSubject(updated.getSubject());
        return updated;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }
}
