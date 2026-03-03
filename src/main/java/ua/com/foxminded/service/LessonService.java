package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.repository.LessonRepository;

@Service
@Transactional
public class LessonService {

    private LessonRepository lessonRepository;
    private ClassroomService classroomService;
    private SubjectService subjectService;
    private GroupService groupService;
    private TeacherService teacherService;
    private LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository, ClassroomService classroomService,
            SubjectService subjectService, GroupService groupService, TeacherService teacherService,
            LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.lessonMapper = lessonMapper;
    }

    public Lesson addOne(LessonCreateUpdateDto dto) {

        Classroom classroom = classroomService.findById(dto.getClassroomId());
        Subject subject = subjectService.findById(dto.getSubjectId());
        Group group = groupService.findById(dto.getGroupId());
        Teacher teacher = teacherService.findById(dto.getTeacherId());

        Lesson lesson = lessonMapper.toEntity(dto, classroom, subject, group, teacher);

        try {
            return lessonRepository.save(lesson);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Lesson with subject: " + subject.getTitle()
                    + " alredy exists in classroom: " + classroom.getName() + "!");
        }
    }

    public Lesson findById(Integer id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
    }

    public List<Lesson> findBySubjectTitleAndWeekDay(String subjectTitle, WeekDay weekDay) {
        return lessonRepository.findBySubject_TitleAndWeekDay(subjectTitle, weekDay);
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
        if (updated.getGroupId() != null) {
            Group group = groupService.findById(updated.getGroupId());
            UpdateUtil.setIfNotNull(group, lesson::setGroup);
        }
        if (updated.getTeacherId() != null) {
            Teacher teacher = teacherService.findById(updated.getTeacherId());
            UpdateUtil.setIfNotNull(teacher, lesson::setTeacher);
        }

        UpdateUtil.setIfNotNull(updated.getClassHour(), lesson::setClassHour);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        return lesson;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }
}
