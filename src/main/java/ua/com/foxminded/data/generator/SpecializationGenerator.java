package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.SpecializationService;
@Component
public class SpecializationGenerator {

    private final SpecializationService service;
    private final Faker faker;

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
                title = faker.university().name();
            } while (usedTitles.contains(title));

            usedTitles.add(title);

            SpecializationCreateUpdateDto dto = new SpecializationCreateUpdateDto();
            dto.setTitle(title);
            dto.setDepartmentId(department.getId());

            try {
                Specialization specialization = service.addOne(dto);
                specializations.add(specialization);
            } catch (DuplicateEntityException e) {
                e.printStackTrace();
            }

        }

        return specializations;
    }
}
