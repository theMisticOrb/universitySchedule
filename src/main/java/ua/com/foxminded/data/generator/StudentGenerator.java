package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.StudentRepository;

@Component
@Profile("dev")
public class StudentGenerator {

    private final StudentRepository repository;
    private final Faker faker;

    public StudentGenerator(StudentRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Student> generateStudents(int studentCount, Group group) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < studentCount; i++) {

            String firstName;
            String middleName;
            String lastName;

            firstName = faker.name().firstName();
            middleName = faker.name().firstName();
            lastName = faker.name().lastName();

            Student student = new Student();
            student.setFirstName(firstName);
            student.setMiddleName(middleName);
            student.setLastName(lastName);
            student.setGroup(group);
            student.setUserRole(UserRole.STUDENT);

            students.add(student);

        }
        students = repository.saveAll(students);

        return students;
    }

}
