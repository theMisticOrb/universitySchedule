package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.StudentService;

@Component
@Profile("dev")
public class StudentGenerator {

    private final StudentService service;
    private final Faker faker;

    public StudentGenerator(StudentService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Student> generateStudents(int studentCount, Group group) {
        List<StudentCreateUpdateDto> students = new ArrayList<>();

        for (int i = 0; i < studentCount; i++) {

            String firstName;
            String middleName;
            String lastName;
            String email;
            String password;

            firstName = faker.name().firstName();
            middleName = faker.name().firstName();
            lastName = faker.name().lastName();
            email = faker.internet().emailAddress();
            password = faker.internet().password();

            StudentCreateUpdateDto student = new StudentCreateUpdateDto();
            Set<UserRole> roles = new HashSet<>();
            roles.add(UserRole.STUDENT);

            student.setFirstName(firstName);
            student.setMiddleName(middleName);
            student.setLastName(lastName);
            student.setRoles(roles);
            student.setEmail(email);
            student.setPassword(password);
            student.setGroupName(group.getName());

            students.add(student);

        }

        return service.addAll(students);
    }

}
