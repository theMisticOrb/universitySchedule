package ua.com.foxminded.data.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.SubjectRepository;

@ExtendWith(MockitoExtension.class)
class SubjectGeneratorTest {

    @Mock
    private SubjectRepository repository;

    private Faker faker;

    private SubjectGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new SubjectGenerator(repository, faker);
    }

    @Test
    void generateSubjects_shouldGenerateRequestedNumberOfSubjects_whenCalled() {

        Teacher teacher = new Teacher();
        teacher.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Subject> result = generator.generateSubjects(140, teacher);

        assertEquals(140, result.size());
    }

    @Test
    void generateSubjects_shouldGenerateUniqueSubjects_whenCalled() {

        Teacher teacher = new Teacher();
        teacher.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Subject> result = generator.generateSubjects(140, teacher);

        Set<String> uniqueKeys = new HashSet<>();

        for (Subject subject : result) {
            String key = subject.getTitle();

            assertTrue(uniqueKeys.add(key));
        }
    }

}
