package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.AcademicDepartmentRepository;

@Component
@Profile("dev")
public class AcademicDepartmentGenerator {

    private final AcademicDepartmentRepository repository;
    private final Faker faker;

    public AcademicDepartmentGenerator(AcademicDepartmentRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<AcademicDepartment> generateDepartment(int departmentCount, Faculty faculty) {
        List<AcademicDepartment> departments = new ArrayList<>();

        for (int i = 0; i < departmentCount; i++) {

            String code = faker.regexify("[A-Z]{3}");

            AcademicDepartment department = new AcademicDepartment();
            department.setTitle(code + "-" + faker.educator().secondarySchool());
            department.setFaculty(faculty);
            department.setCode(code);

            departments.add(department);

        }
        departments = repository.saveAll(departments);
        return departments;
    }

}
