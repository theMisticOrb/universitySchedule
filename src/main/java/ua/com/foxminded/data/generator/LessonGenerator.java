package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

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

        for (int i = 0; i < lessonCountPerWeek; i++) {

            Classroom classroom = classrooms.get(faker.random().nextInt(classrooms.size()));
            Teacher teacher = teachers.get(faker.random().nextInt(teachers.size()));
            WeekDay day = days[i % days.length];
            ClassHour hour = hours[i % hours.length];

            Lesson lesson = new Lesson();
            lesson.setGroup(group);
            lesson.setTeacher(teacher);
            lesson.setClassHour(hour);
            lesson.setClassroom(classroom);

            List<Subject> subjects = teacher.getSubjects();
            lesson.setSubject(subjects.get(faker.random().nextInt(subjects.size())));
            lesson.setWeekDay(day);

            lessons.add(lesson);
        }

        return repository.saveAll(lessons);
    }

}
