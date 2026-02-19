package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.sdjpa.ClassroomRepository;

@Component
@Profile("dev")
public class ClassroomGenerator {

    private final ClassroomRepository repository;
    private final Faker faker;

    public ClassroomGenerator(ClassroomRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Classroom> generateClassrooms(int lessonCount) {
        List<Classroom> classrooms = new ArrayList<>();

        for (int i = 0; i < lessonCount; i++) {

            Classroom classroom = new Classroom();
            classroom.setName(faker.educator().campus());

            classrooms.add(classroom);

        }
        repository.saveAll(classrooms);

        return classrooms;
    }

}
