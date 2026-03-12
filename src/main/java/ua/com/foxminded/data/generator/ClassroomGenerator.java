package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.repository.ClassroomRepository;

@Component
@Profile("dev")
public class ClassroomGenerator {

    private final ClassroomRepository repository;
    private final Faker faker;

    public ClassroomGenerator(ClassroomRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Classroom> generateClassrooms(int classroomCount) {
        List<Classroom> classrooms = new ArrayList<>();
        Set<String> names = new HashSet<>();

        while (names.size() < classroomCount) {
            String name = faker.regexify("[A-Z]{3}") + faker.number().numberBetween(100, 500);

            if (names.add(name)) {
                Classroom classroom = new Classroom();
                classroom.setName(name);
                classrooms.add(classroom);
            }

        }
        classrooms = repository.saveAll(classrooms);

        return classrooms;
    }

}
