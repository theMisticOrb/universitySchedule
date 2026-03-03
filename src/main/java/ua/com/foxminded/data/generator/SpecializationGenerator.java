package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.repository.SpecializationRepository;

@Component
@Profile("dev")
public class SpecializationGenerator {

    private final SpecializationRepository repository;
    private final Faker faker;

    public SpecializationGenerator(SpecializationRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Specialization> generateSpecialization(int specializationCount, AcademicDepartment department) {
        List<Specialization> specializations = new ArrayList<>();

        for (int i = 0; i < specializationCount; i++) {

            Specialization specialization = new Specialization();
            specialization.setTitle(faker.university().name() + " - " + UUID.randomUUID());
            specialization.setDepartment(department);
            specialization.setCode(faker.regexify("[A-Z]{2}"));

            specializations.add(specialization);

        }
        specializations = repository.saveAll(specializations);

        return specializations;
    }
}
