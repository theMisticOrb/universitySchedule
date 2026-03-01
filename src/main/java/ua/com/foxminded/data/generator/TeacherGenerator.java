package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.TeacherRepository;

@Component
@Profile("dev")
public class TeacherGenerator {

    private final TeacherRepository repository;
    private final Faker faker;

    public TeacherGenerator(TeacherRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Teacher> generateTeachers(int teacherCount, AcademicDepartment department) {
        List<Teacher> teachers = new ArrayList<>();

        for (int i = 0; i < teacherCount; i++) {

            String firstName;
            String middleName;
            String lastName;

            firstName = faker.name().firstName();
            middleName = faker.name().firstName();
            lastName = faker.name().lastName();

            Teacher teacher = new Teacher();
            teacher.setFirstName(firstName);
            teacher.setMiddleName(middleName);
            teacher.setLastName(lastName);
            teacher.setDepartment(department);

            teachers.add(teacher);

        }
        teachers = repository.saveAll(teachers);

        return teachers;
    }

}
