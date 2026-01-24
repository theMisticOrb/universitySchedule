package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.LessonDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.sdjpa.LessonRepository;

@Service
@Transactional
public class LessonService {

    private LessonRepository lessonRepository;
    private ClassroomService classroomService;
    private SubjectService subjectService;

    protected LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson addOne(LessonDto dto) {
        Lesson lesson = new Lesson();
        Classroom classroom = classroomService.findById(dto.getClassroomId());
        Subject subject = subjectService.findById(dto.getSubjectId());
        
        lesson.setClassHour(dto.getClassHour());
        lesson.setClassroom(classroom);
        lesson.setSubject(subject);
        lesson.setWeekDay(dto.getWeekDay());
        return lessonRepository.save(lesson);
    }

    public Lesson findById(Integer id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
    }

    public Optional<Lesson> findByTitle(String subjectTitle) {
        return lessonRepository.findBySubject(subjectTitle);
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson update(LessonDto updated, Integer id) {
        Lesson lesson = findById(id);
        Classroom classroom = classroomService.findById(updated.getClassroomId());
        Subject subject = subjectService.findById(updated.getSubjectId());
        
        UpdateUtil.setIfNotNull(updated.getClassHour(), lesson::setClassHour);
        UpdateUtil.setIfNotNull(classroom, lesson::setClassroom);
        UpdateUtil.setIfNotNull(subject, lesson::setSubject);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        return lesson;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }
}
