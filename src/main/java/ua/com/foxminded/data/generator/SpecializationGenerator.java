package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

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

            String code = faker.regexify("[A-Z]{4}");
            Specialization specialization = new Specialization();
            specialization.setTitle(code + " - " + faker.university().name());
            specialization.setDepartment(department);
            specialization.setCode(code);

            specializations.add(specialization);

        }
        specializations = repository.saveAll(specializations);

        return specializations;
    }
}
