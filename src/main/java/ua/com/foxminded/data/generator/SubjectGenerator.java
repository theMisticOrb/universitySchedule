package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.SubjectRepository;

@Component
@Profile("dev")
public class SubjectGenerator {

    private final SubjectRepository repository;
    private final Faker faker;

    public SubjectGenerator(SubjectRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Subject> generateSubjects(int subjectCount, Teacher teacher) {
        List<Subject> subjects = new ArrayList<>();

        for (int i = 0; i < subjectCount; i++) {

            Subject subject = new Subject();
            subject.setTitle(faker.educator().course() + " - " + UUID.randomUUID());
            subject.setTeacher(teacher);

            teacher.getSubjects().add(subject);

            subjects.add(subject);

        }
        subjects = repository.saveAll(subjects);

        return subjects;
    }

}
