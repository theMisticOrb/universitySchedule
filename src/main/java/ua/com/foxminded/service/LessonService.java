package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.sdjpa.LessonRepository;

@Service
@Transactional
public class LessonService {

    private LessonRepository lessonRepository;

    protected LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson addOne(Lesson lesson) {
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
        UpdateUtil.setIfNotNull(updated.getClassHours(), lesson::setClassHours);
        UpdateUtil.setIfNotNull(updated.getClassroom(), lesson::setClassroom);
        UpdateUtil.setIfNotNull(updated.getSubject(), lesson::setSubject);
        return lesson;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }
}
