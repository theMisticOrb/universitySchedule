package ua.com.foxminded.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import ua.com.foxminded.repository.TeacherRepository;

@Service
@Transactional
public class LessonService {

    private LessonRepository lessonRepository;
    private TeacherRepository teacherRepository;
    private ClassroomService classroomService;
    private SubjectService subjectService;
    private GroupService groupService;
    private LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository, ClassroomService classroomService,
            SubjectService subjectService, GroupService groupService, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.classroomService = classroomService;
        this.subjectService = subjectService;
        this.groupService = groupService;
        this.lessonMapper = lessonMapper;
    }

    public Lesson addOne(LessonCreateUpdateDto dto, Integer teacherId) {

        if (lessonRepository.existsByGroup_NameAndWeekDayAndClassHour(dto.getGroupName(), dto.getWeekDay(),
                dto.getClassHour())) {
            throw new IllegalArgumentException("The group already has a class at that time");
        }

        if (lessonRepository.existsByTeacher_IdAndWeekDayAndClassHour(teacherId, dto.getWeekDay(),
                dto.getClassHour())) {
            throw new IllegalArgumentException("You already have a class at that time");
        }

        Classroom classroom = classroomService.findByName(dto.getClassroomName());
        Subject subject = subjectService.findByTitle(dto.getSubjectName());
        Group group = groupService.findByName(dto.getGroupName());
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found!"));

        Lesson lesson = lessonMapper.toEntity(dto, classroom, subject, group, teacher);

        try {
            return lessonRepository.save(lesson);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Lesson with subject: " + subject.getTitle()
                    + " alredy exists in classroom: " + classroom.getName() + " on this day and hour!");
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

        if (updated.getClassroomName() != null) {
            Classroom classroom = classroomService.findByName(updated.getClassroomName());
            UpdateUtil.setIfNotNull(classroom, lesson::setClassroom);
        }
        if (updated.getSubjectName() != null) {
            Subject subject = subjectService.findByTitle(updated.getSubjectName());
            UpdateUtil.setIfNotNull(subject, lesson::setSubject);
        }
        if (updated.getGroupName() != null) {
            Group group = groupService.findByName(updated.getGroupName());
            UpdateUtil.setIfNotNull(group, lesson::setGroup);
        }

        UpdateUtil.setIfNotNull(updated.getClassHour(), lesson::setClassHour);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        return lesson;
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }

    public Map<WeekDay, List<Lesson>> getWeeklyScheduleForTeacher(Integer teacherId) {
        List<Lesson> lessons = lessonRepository.findByTeacher_Id(teacherId);

        return Arrays.stream(WeekDay.values())
                .collect(Collectors.toMap(day -> day, day -> lessons.stream().filter(l -> l.getWeekDay() == day)
                        .sorted(Comparator.comparing(l -> l.getClassHour().getStart())).collect(Collectors.toList()),
                        (a, b) -> a, LinkedHashMap::new));

    }
}
