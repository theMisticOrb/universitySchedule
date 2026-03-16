package ua.com.foxminded.data.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.repository.SubjectRepository;

@Component
@Profile("dev")
public class SubjectGenerator {

    private final SubjectRepository repository;
    private final Faker faker;
    private final Set<String> usedTitles = new HashSet<>();

    public SubjectGenerator(SubjectRepository repository, Faker faker) {
        this.repository = repository;
        this.faker = faker;
    }

    public List<Subject> generateSubjects(int subjectCount, Teacher teacher, AcademicDepartment department) {
        List<Subject> subjects = new ArrayList<>();

        int attempt = 0;
        int maxAttempt = 5000;

        while (subjects.size() < subjectCount && attempt < maxAttempt) {

            String subjectTitle = faker.educator().course() + " (" + department.getCode() + "-" + (attempt + 1) + ")";
            attempt++;

            if (usedTitles.contains(subjectTitle)) {
                continue;
            }

            Subject subject = new Subject();
            subject.setTitle(subjectTitle);
            subject.setTeacher(teacher);
            subject.setDepartment(department);

            teacher.getSubjects().add(subject);
            department.getSubjects().add(subject);
            usedTitles.add(subjectTitle);
            subjects.add(subject);

        }
        subjects = repository.saveAll(subjects);

        return subjects;
    }

}
