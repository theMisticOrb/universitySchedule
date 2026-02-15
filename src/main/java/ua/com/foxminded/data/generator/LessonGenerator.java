package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.LessonService;

@Component
@Profile("dev")
public class LessonGenerator {

    private final LessonService service;
    private final Faker faker;
    private final Logger log = LoggerFactory.getLogger(LessonGenerator.class);

    public LessonGenerator(LessonService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Lesson> generateLesson(int lessonCount, Subject subject, List<Classroom> classrooms) {
        List<Lesson> lessons = new ArrayList<>();

        for (int i = 0; i < lessonCount; i++) {

            Classroom classroom = classrooms.get(faker.random().nextInt(classrooms.size()));

            LessonCreateUpdateDto dto = new LessonCreateUpdateDto();
            dto.setClassHour(ClassHour.FIFTH_CLASS);
            dto.setClassroomId(classroom.getId());
            dto.setSubjectId(subject.getId());
            dto.setWeekDay(WeekDay.MONDAY);

            try {
                Lesson lesson = service.addOne(dto);
                lessons.add(lesson);
            } catch (DuplicateEntityException e) {
                log.warn("Duplicate entity: {}", subject.getTitle());
            }

        }

        return lessons;
    }

}
