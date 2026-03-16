package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.repository.LessonRepository;

@Component
@Profile("dev")
public class LessonGenerator {

    private final LessonRepository repository;
    private final Faker faker;
    private final Set<String> teacherSlots = new HashSet<>();
    private final Set<String> classroomSlots = new HashSet<>();
    private final Set<String> groupSlots = new HashSet<>();

    public LessonGenerator(LessonRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Lesson> generateLessons(int lessonCountPerWeek, Group group, List<Teacher> teachers,
            List<Classroom> classrooms) {
        List<Lesson> lessons = new ArrayList<>();
        ClassHour[] hours = ClassHour.values();
        WeekDay[] days = WeekDay.values();

        int totalSlots = days.length * hours.length;
        if (lessonCountPerWeek > totalSlots) {
            throw new IllegalArgumentException("Cannot generate more lessons per week than available slots");
        }

        int attempt = 0;
        int maxAttempt = 5000;

        while (lessons.size() < lessonCountPerWeek && attempt < maxAttempt) {

            attempt++;

            Classroom classroom = classrooms.get(faker.random().nextInt(classrooms.size()));
            Teacher teacher = teachers.get(faker.random().nextInt(teachers.size()));
            WeekDay day = days[faker.random().nextInt(days.length)];
            ClassHour hour = hours[faker.random().nextInt(hours.length)];

            String teacherKey = slotKey(teacher.getId(), day, hour);
            String classroomKey = slotKey(classroom.getId(), day, hour);
            String groupKey = slotKey(group.getId(), day, hour);

            if (teacherSlots.contains(teacherKey) || classroomSlots.contains(classroomKey)
                    || groupSlots.contains(groupKey)) {
                continue;
            }

            Lesson lesson = new Lesson();
            lesson.setGroup(group);
            lesson.setTeacher(teacher);
            lesson.setClassHour(hour);
            lesson.setClassroom(classroom);

            List<Subject> subjects = teacher.getSubjects();
            lesson.setSubject(subjects.get(faker.random().nextInt(subjects.size())));
            lesson.setWeekDay(day);

            lessons.add(lesson);

            teacherSlots.add(teacherKey);
            classroomSlots.add(classroomKey);
            groupSlots.add(groupKey);
        }

        return repository.saveAll(lessons);
    }

    private String slotKey(Object id, WeekDay day, ClassHour hour) {
        return id + "_" + day + "_" + hour;

    }

}
