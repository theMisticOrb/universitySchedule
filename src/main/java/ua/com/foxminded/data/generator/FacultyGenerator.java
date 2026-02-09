package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.FacultyService;

@Component
public class FacultyGenerator {

    private final FacultyService service;
    private final Faker faker;

    public FacultyGenerator(FacultyService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Faculty> generateFaculty(int facultyCount) {
        List<Faculty> faculties = new ArrayList<>();
        Set<String> usedTitles = new HashSet<>();

        for (int i = 0; i < facultyCount; i++) {
            String title;
            do {
                title = faker.university().name();
            } while (usedTitles.contains(title));

            usedTitles.add(title);

            FacultyCreateUpdateDto dto = new FacultyCreateUpdateDto();
            dto.setTitle(title);

            try {
                Faculty faculty = service.addOne(dto);
                faculties.add(faculty);
            } catch (DuplicateEntityException e) {
                e.printStackTrace();
            }

        }

        return faculties;
    }

}
