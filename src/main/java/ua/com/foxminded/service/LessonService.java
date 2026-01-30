package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
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

    public LessonService(LessonRepository lessonRepository, ClassroomService classroomService,
            SubjectService subjectService) {
        this.lessonRepository = lessonRepository;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
    }

    public Lesson addOne(LessonCreateUpdateDto dto) {
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
        return lessonRepository.findBySubject_Title(subjectTitle);
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Lesson update(LessonCreateUpdateDto updated, Integer id) {
        Lesson lesson = findById(id);

        if (updated.getClassroomId() != null) {
            Classroom classroom = classroomService.findById(updated.getClassroomId());
            UpdateUtil.setIfNotNull(classroom, lesson::setClassroom);
        }
        if (updated.getSubjectId() != null) {
            Subject subject = subjectService.findById(updated.getSubjectId());
            UpdateUtil.setIfNotNull(subject, lesson::setSubject);
        }

        UpdateUtil.setIfNotNull(updated.getClassHour(), lesson::setClassHour);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        return lesson;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }
}
