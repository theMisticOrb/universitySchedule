package ua.com.foxminded.data.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.repository.LessonRepository;

@ExtendWith(MockitoExtension.class)
class LessonGeneratorTest {

    @Mock
    private LessonRepository repository;

    private Faker faker;

    private LessonGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new LessonGenerator(repository, faker);
    }

    @Test
    void generateLesson_shouldGenerateRequestedNumberOfLessons_whenCalled() {

        Subject subject = new Subject();
        subject.setId(1);

        Classroom classroom = new Classroom();
        classroom.setId(1);

        List<Classroom> classrooms = Arrays.asList(classroom);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Lesson> result = generator.generateLesson(103, subject, classrooms);

        assertEquals(103, result.size());
        verify(repository).saveAll(any());
    }

    @Test
    void generateLesson_shouldGenerateUniqueLessons_whenCalled() {

        Subject subject = new Subject();
        subject.setId(1);

        Classroom classroom = new Classroom();
        classroom.setId(1);

        List<Classroom> classrooms = Arrays.asList(classroom);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Lesson> result = generator.generateLesson(105, subject, classrooms);

        Set<String> uniqueKeys = new HashSet<>();

        for (Lesson lesson : result) {
            String key = lesson.getSubject().getId() + "_" + lesson.getClassroom().getId() + "_" + lesson.getWeekDay()
                    + "_" + lesson.getClassHour();

            assertTrue(uniqueKeys.add(key));
        }
    }

    @Test
    void generateLesson_shouldThrowExceptionWhenRequestedMoreThanPossible_whenCalled() {

        Subject subject = new Subject();
        subject.setId(1);

        Classroom classroom = new Classroom();
        classroom.setId(1);

        List<Classroom> classrooms = Arrays.asList(classroom);

        int maxCombinations = classrooms.size() * ClassHour.values().length * WeekDay.values().length;

        assertThrows(IllegalArgumentException.class,
                () -> generator.generateLesson(maxCombinations + 1, subject, classrooms));
    }

}
