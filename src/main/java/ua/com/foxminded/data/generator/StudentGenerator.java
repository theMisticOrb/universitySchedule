package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.StudentService;


@Component
public class StudentGenerator {

    private final StudentService service;
    private final Faker faker;

    public StudentGenerator(StudentService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Student> generateStudents(int studentCount, Group group) {
        List<Student> students = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < studentCount; i++) {

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

            StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
            dto.setFirstName(firstName);
            dto.setMiddleName(middleName);
            dto.setLastName(lastName);
            dto.setGroupId(group.getId());

            try {
                Student student = service.addOne(dto);
                students.add(student);
            } catch (DuplicateEntityException e) {
                e.printStackTrace();
            }

        }

        return students;
    }

}
