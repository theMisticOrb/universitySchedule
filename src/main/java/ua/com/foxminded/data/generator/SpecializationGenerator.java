package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.SpecializationService;
@Component
@Profile("dev")
public class SpecializationGenerator {

    private final SpecializationService service;
    private final Faker faker;
    private final Logger log = LoggerFactory.getLogger(SpecializationGenerator.class);

    public SpecializationGenerator(SpecializationService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    
    public List<Specialization> generateSpecialization(int specializationCount, AcademicDepartment department) {
        List<Specialization> specializations = new ArrayList<>();
        Set<String> usedTitles = new HashSet<>();

        for (int i = 0; i < specializationCount; i++) {
            String title;
            do {
                title = faker.university().name() + " - " + department.getId() + "-" + i;
            } while (usedTitles.contains(title));

            usedTitles.add(title);

            SpecializationCreateUpdateDto dto = new SpecializationCreateUpdateDto();
            dto.setTitle(title);
            dto.setDepartmentId(department.getId());

            try {
                Specialization specialization = service.addOne(dto);
                specializations.add(specialization);
            } catch (DuplicateEntityException e) {
                log.warn("Duplicate entity: {}", title);
            }

        }

        return specializations;
    }
}
