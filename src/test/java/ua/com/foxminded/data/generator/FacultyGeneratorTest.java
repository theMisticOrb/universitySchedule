package ua.com.foxminded.data.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.FacultyRepository;

@ExtendWith(MockitoExtension.class)
class FacultyGeneratorTest {

    @Mock
    private FacultyRepository repository;

    private Faker faker;

    private FacultyGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new FacultyGenerator(repository, faker);
    }

    @Test
    void generateFaculties_shouldGenerateRequestedNumberOfFaculties_whenCalled() {

        Faculty faculty = new Faculty();
        faculty.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        int facultyCount = 30;
        List<Faculty> result = generator.generateFaculties(facultyCount);

        assertEquals(facultyCount, result.size());
        verify(repository).saveAll(any());
    }

}
