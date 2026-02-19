package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.sdjpa.LessonRepository;

@Component
@Profile("dev")
public class LessonGenerator {

    private final LessonRepository repository;
    private final Faker faker;

    public LessonGenerator(LessonRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Lesson> generateLesson(int lessonCount, Subject subject, List<Classroom> classrooms) {
        List<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < lessonCount; i++) {

            Classroom classroom = classrooms.get(faker.random().nextInt(classrooms.size()));

            Lesson lesson = new Lesson();
            lesson.setClassHour(ClassHour.FIFTH_CLASS);
            lesson.setClassroom(classroom);
            lesson.setSubject(subject);
            lesson.setWeekDay(WeekDay.MONDAY);

            lessons.add(lesson);

        }
        repository.saveAll(lessons);

        return lessons;
    }

}
