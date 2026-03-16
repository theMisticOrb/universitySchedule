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

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.AcademicDepartmentRepository;

@ExtendWith(MockitoExtension.class)
class AcademicDepartmentGeneratorTest {

    @Mock
    private AcademicDepartmentRepository repository;

    private Faker faker;

    private AcademicDepartmentGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new AcademicDepartmentGenerator(repository, faker);
    }

    @Test
    void generateDepartments_shouldGenerateRequestedNumberOfDepartments_whenCalled() {

        Faculty faculty = new Faculty();
        faculty.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<AcademicDepartment> result = generator.generateDepartment(140, faculty);

        assertEquals(140, result.size());
    }

    @Test
    void generateDepartments_shouldGenerateUniqueDepartments_whenCalled() {

        Faculty faculty = new Faculty();
        faculty.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<AcademicDepartment> result = generator.generateDepartment(140, faculty);

        Set<String> uniqueKeys = new HashSet<>();

        for (AcademicDepartment department : result) {
            String key = department.getTitle();

            assertTrue(uniqueKeys.add(key));
        }
    }

}
