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

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.AcademicDepartmentService;

@Component
@Profile("dev")
public class AcademicDepartmentGenerator {

    private final AcademicDepartmentService service;
    private final Faker faker;
    private final Logger log = LoggerFactory.getLogger(AcademicDepartmentGenerator.class);

    public AcademicDepartmentGenerator(AcademicDepartmentService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<AcademicDepartment> generateDepartment(int departmentCount, Faculty faculty) {
        List<AcademicDepartment> departments = new ArrayList<>();
        Set<String> usedTitles = new HashSet<>();

        for (int i = 0; i < departmentCount; i++) {
            String title;
            do {
                title = faker.educator().secondarySchool() + " - " + faculty.getId() + "-" + i;
            } while (usedTitles.contains(title));

            usedTitles.add(title);

            AcademicDepartmentCreateUpdateDto dto = new AcademicDepartmentCreateUpdateDto();
            dto.setTitle(title);
            dto.setFacultyId(faculty.getId());

            try {
                AcademicDepartment department = service.addOne(dto);
                departments.add(department);
            } catch (DuplicateEntityException e) {
                log.warn("Duplicate department: {}", title);
            }

        }

        return departments;
    }

}
