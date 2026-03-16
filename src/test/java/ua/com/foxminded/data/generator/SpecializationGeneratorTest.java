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
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.repository.SpecializationRepository;

@ExtendWith(MockitoExtension.class)
class SpecializationGeneratorTest {

    @Mock
    private SpecializationRepository repository;

    private Faker faker;

    private SpecializationGenerator generator;

    @BeforeEach
    void setUp() {
        faker = new Faker(new Random(42));
        generator = new SpecializationGenerator(repository, faker);
    }

    @Test
    void generateSpecializations_shouldGenerateRequestedNumberOfSpecializations_whenCalled() {

        AcademicDepartment department = new AcademicDepartment();
        department.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Specialization> result = generator.generateSpecialization(140, department);

        assertEquals(140, result.size());
    }

    @Test
    void generateSpecializations_shouldGenerateUniqueSpecializations_whenCalled() {

        AcademicDepartment department1 = new AcademicDepartment();
        department1.setId(1);

        when(repository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Specialization> result = generator.generateSpecialization(140, department1);

        Set<String> uniqueKeys = new HashSet<>();

        for (Specialization department : result) {
            String key = department.getTitle();

            assertTrue(uniqueKeys.add(key));
        }
    }

}
