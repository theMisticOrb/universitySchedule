package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.TeacherService;

@Component
@Profile("dev")
public class TeacherGenerator {

    private final TeacherService service;
    private final Faker faker;

    public TeacherGenerator(TeacherService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Teacher> generateTeachers(int teacherCount, AcademicDepartment department) {
        List<TeacherCreateUpdateDto> teachers = new ArrayList<>();

        for (int i = 0; i < teacherCount; i++) {

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

            TeacherCreateUpdateDto teacher = new TeacherCreateUpdateDto();
            Set<UserRole> roles = new HashSet<>();
            roles.add(UserRole.TEACHER);
            
            teacher.setFirstName(firstName);
            teacher.setMiddleName(middleName);
            teacher.setLastName(lastName);
            teacher.setDepartmentId(department.getId());
            teacher.setUserRoles(roles);
            teacher.setEmail(email);
            teacher.setPassword(password);

            teachers.add(teacher);

        }

        return service.addAll(teachers);
    }

}
