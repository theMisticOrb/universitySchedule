package ua.com.foxminded.data.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
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
    void generateLessons_shouldGenerateRequestedNumberOfLessons_whenCalled() {

        Group group = new Group();
        group.setId(1);

        Subject subject = new Subject();
        subject.setId(1);
        List<Subject> subjects = Arrays.asList(subject);

        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setSubjects(subjects);

        Classroom classroom = new Classroom();
        classroom.setId(1);

        List<Classroom> classrooms = Arrays.asList(classroom);
        List<Teacher> teachers = Arrays.asList(teacher);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        int lessonsPerWeek = 30;
        List<Lesson> result = generator.generateLessons(lessonsPerWeek, group, teachers, classrooms);

        assertEquals(lessonsPerWeek, result.size());
        verify(repository).saveAll(any());
    }

    @Test
    void generateLessons_shouldThrowExceptionWhenRequestedMoreThanPossible_whenCalled() {

        Group group = new Group();
        group.setId(1);

        Teacher teacher = new Teacher();
        teacher.setId(1);

        Classroom classroom = new Classroom();
        classroom.setId(1);

        List<Classroom> classrooms = Arrays.asList(classroom);
        List<Teacher> teachers = Arrays.asList(teacher);

        int moreThanPossiblelessonsPerWeek = 50;

        assertThrows(IllegalArgumentException.class,
                () -> generator.generateLessons(moreThanPossiblelessonsPerWeek, group, teachers, classrooms));
    }

}
