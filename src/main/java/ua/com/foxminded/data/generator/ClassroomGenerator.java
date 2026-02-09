package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.ClassroomService;

public class ClassroomGenerator {

    private final ClassroomService service;
    private final Faker faker;

    public ClassroomGenerator(ClassroomService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Classroom> generateClassrooms(int lessonCount) {
        List<Classroom> classrooms = new ArrayList<>();
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < lessonCount; i++) {
            String name;
            do {
                name = faker.educator().campus();
            } while (usedNames.contains(name));

            usedNames.add(name);

            ClassroomCreateUpdateDto dto = new ClassroomCreateUpdateDto();
            dto.setName(name);

            try {
                Classroom classroom = service.addOne(dto);
                classrooms.add(classroom);
            } catch (DuplicateEntityException e) {
                e.printStackTrace();
            }

        }

        return classrooms;
    }

}
