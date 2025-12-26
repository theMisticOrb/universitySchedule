package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.helpers.UpdateUtils;
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
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson update(Lesson updated) {
        Lesson lesson = findById(updated.getId());
        UpdateUtils.setIfNotNull(updated.getClassHours(), lesson::setClassHours);
        UpdateUtils.setIfNotNull(updated.getClassroom(), lesson::setClassroom);
        UpdateUtils.setIfNotNull(updated.getSubject(), lesson::setSubject);
        return updated;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }
}
