package ua.com.foxminded.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.repository.LessonRepository;
import ua.com.foxminded.repository.TeacherRepository;

@Service
@Transactional
public class LessonService implements ReadUpdateDeleteService<Lesson, LessonCreateUpdateDto, Integer> {

    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomService classroomService;
    private final SubjectService subjectService;
    private final GroupService groupService;
    private final LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository, TeacherRepository teacherRepository,
            ClassroomService classroomService, SubjectService subjectService, GroupService groupService,
            LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
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

        if (lessonRepository.existsByClassroom_NameAndWeekDayAndClassHour(dto.getClassroomName(), dto.getWeekDay(),
                dto.getClassHour())) {
            throw new IllegalArgumentException("The classroom is already occupied at that time");
        }

        Classroom classroom = classroomService.findByName(dto.getClassroomName());
        Subject subject = subjectService.findByTitle(dto.getSubjectTitle());
        Group group = groupService.findByName(dto.getGroupName());
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found!"));

        Lesson lesson = lessonMapper.toEntity(dto, classroom, subject, group, teacher);

        try {
            return lessonRepository.saveAndFlush(lesson);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Lesson with subject: " + subject.getTitle()
                    + " alredy exists in classroom: " + classroom.getName() + " on this day and hour!");
        }
    }

    @Override
    public Lesson findById(Integer id) {
        return lessonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
    }

    public List<Lesson> findBySubjectTitleAndWeekDay(String subjectTitle, WeekDay weekDay) {
        return lessonRepository.findBySubject_TitleAndWeekDay(subjectTitle, weekDay);
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson fullUpdate(LessonCreateUpdateDto updated, Integer id) {
        Lesson lesson = findById(id);

        if (updated.getClassroomName() != null) {
            Classroom classroom = classroomService.findByName(updated.getClassroomName());
            UpdateUtil.setIfNotNull(classroom, lesson::setClassroom);
        }
        if (updated.getSubjectTitle() != null) {
            Subject subject = subjectService.findByTitle(updated.getSubjectTitle());
            UpdateUtil.setIfNotNull(subject, lesson::setSubject);
        }
        if (updated.getGroupName() != null) {
            Group group = groupService.findByName(updated.getGroupName());
            UpdateUtil.setIfNotNull(group, lesson::setGroup);
        }

        UpdateUtil.setIfNotNull(updated.getClassHour(), lesson::setClassHour);
        UpdateUtil.setIfNotNull(updated.getWeekDay(), lesson::setWeekDay);
        return lesson;
    }

    @Override
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

    public Map<WeekDay, List<Lesson>> getWeeklyScheduleForGroup(String groupName) {

        List<Lesson> lessons = lessonRepository.findByGroup_Name(groupName);

        return Arrays.stream(WeekDay.values())
                .collect(Collectors.toMap(day -> day, day -> lessons.stream().filter(l -> l.getWeekDay() == day)
                        .sorted(Comparator.comparing(l -> l.getClassHour().getStart())).collect(Collectors.toList()),
                        (a, b) -> a, LinkedHashMap::new));
    }

    public Set<Integer> findOccupiedClassroomIds(WeekDay weekDay, ClassHour classHour) {
        return lessonRepository.findByWeekDayAndClassHour(weekDay, classHour).stream()
                .map(lesson -> lesson.getClassroom().getId()).collect(Collectors.toSet());
    }

}
