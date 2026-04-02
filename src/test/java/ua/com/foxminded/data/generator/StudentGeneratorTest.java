package ua.com.foxminded.data.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentGeneratorTest {

    @Mock
    private StudentService service;

    private Faker faker;

    private StudentGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new StudentGenerator(service, faker);
    }

    @Test
    void generateStudents_shouldGenerateRequestedNumberOfStudents_whenCalled() {

        Group group = new Group();
        group.setId(1);

        when(service.addAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Student> result = generator.generateStudents(140, group);

        assertEquals(140, result.size());
    }

}
