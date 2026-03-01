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

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.repository.ClassroomRepository;

@ExtendWith(MockitoExtension.class)
class ClassroomGeneratorTest {

    @Mock
    private ClassroomRepository repository;

    private Faker faker;

    private ClassroomGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new ClassroomGenerator(repository, faker);
    }

    @Test
    void generateClassrooms_shouldGenerateRequestedNumberOfClassrooms_whenCalled() {

        Classroom classroom = new Classroom();
        classroom.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        int classroomsPerDepartment = 30;
        List<Classroom> result = generator.generateClassrooms(classroomsPerDepartment);

        assertEquals(classroomsPerDepartment, result.size());
        verify(repository).saveAll(any());
    }

}
