package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

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

        while (classrooms.size() < classroomCount) {

            Classroom classroom = new Classroom();
            classroom.setName(faker.code().ean8());
            classrooms.add(classroom);

        }
        classrooms = repository.saveAll(classrooms);

        return classrooms;
    }

}
