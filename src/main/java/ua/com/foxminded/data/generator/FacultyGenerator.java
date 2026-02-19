package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Component
@Profile("dev")
public class FacultyGenerator {

    private final FacultyRepository repository;
    private final Faker faker;

    public FacultyGenerator(FacultyRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Faculty> generateFaculty(int facultyCount) {
        List<Faculty> faculties = new ArrayList<>();

        for (int i = 0; i < facultyCount; i++) {

            Faculty faculty = new Faculty();
            faculty.setTitle(faker.university().name());

            faculties.add(faculty);

        }
        repository.saveAll(faculties);

        return faculties;
    }

}
