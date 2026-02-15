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

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.TeacherService;

@Component
@Profile("dev")
public class TeacherGenerator {

    private final TeacherService service;
    private final Faker faker;
    private final Logger log = LoggerFactory.getLogger(TeacherGenerator.class);

    public TeacherGenerator(TeacherService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Teacher> generateTeachers(int teacherCount, AcademicDepartment department) {
        List<Teacher> teachers = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < teacherCount; i++) {

            String firstName;
            String middleName;
            String lastName;
            String fullName;

            StringBuilder fullNameBuilder = new StringBuilder();

            do {
                firstName = faker.name().firstName();
                middleName = faker.name().firstName();
                lastName = faker.name().firstName();

                fullName = fullNameBuilder.append(firstName + " " + middleName + " " + lastName).toString();

            } while (usedNames.contains(fullName));

            usedNames.add(fullName);

            TeacherCreateUpdateDto dto = new TeacherCreateUpdateDto();
            dto.setFirstName(firstName);
            dto.setMiddleName(middleName);
            dto.setLastName(lastName);
            dto.setDepartmentId(department.getId());

            try {
                Teacher teacher = service.addOne(dto);
                teachers.add(teacher);
            } catch (DuplicateEntityException e) {
                log.warn("Duplicate entity: {}", fullName);
            }

        }

        return teachers;
    }

}
