package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javafaker.Faker;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.SubjectService;

public class SubjectGenerator {

    private final SubjectService service;
    private final Faker faker;

    public SubjectGenerator(SubjectService service, Faker faker) {
        this.service = service;
        this.faker = faker;
    }

    public List<Subject> generateSubjects(int subjectCount, Teacher teacher) {
        List<Subject> subjects = new ArrayList<>();
        Set<String> usedTitles = new HashSet<>();

        for (int i = 0; i < subjectCount; i++) {
            String title;
            do {
                title = faker.educator().course();
            } while (usedTitles.contains(title));

            usedTitles.add(title);

            SubjectCreateUpdateDto dto = new SubjectCreateUpdateDto();
            dto.setTitle(title);
            dto.setTeacherId(teacher.getId());

            try {
                Subject subject = service.addOne(dto);
                subjects.add(subject);
            } catch (DuplicateEntityException e) {
                e.printStackTrace();
            }

        }

        return subjects;
    }

}
