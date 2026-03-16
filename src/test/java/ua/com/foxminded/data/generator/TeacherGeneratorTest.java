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

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherGeneratorTest {

    @Mock
    private TeacherRepository repository;

    private Faker faker;

    private TeacherGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new TeacherGenerator(repository, faker);
    }

    @Test
    void generateTeachers_shouldGenerateRequestedNumberOfTeachers_whenCalled() {

        AcademicDepartment department = new AcademicDepartment();
        department.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Teacher> result = generator.generateTeachers(140, department);

        assertEquals(140, result.size());
    }

}
