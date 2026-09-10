package ua.com.foxminded.service.suppport;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.SpecializationRepository;
import ua.com.foxminded.repository.SubjectRepository;
import ua.com.foxminded.repository.TeacherRepository;

@Component
public class TestDataFactory {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AcademicDepartmentRepository departmentRepository;
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    public static String uniqueName(String prefix) {
        return prefix + "-" + COUNTER.incrementAndGet();
    }

    public Faculty persistFaculty() {
        return persistFaculty(uniqueName("Faculty"));
    }

    public Faculty persistFaculty(String title) {
        Faculty faculty = new Faculty();
        faculty.setTitle(title);
        faculty.setCode(uniqueName("FC"));
        return facultyRepository.save(faculty);
    }

    public AcademicDepartment persistDepartment(Faculty faculty) {
        return persistDepartment(uniqueName("Department"), faculty);
    }

    public AcademicDepartment persistDepartment(String title, Faculty faculty) {
        AcademicDepartment department = new AcademicDepartment();
        department.setTitle(title);
        department.setCode(uniqueName("DP"));
        department.setFaculty(faculty);
        return departmentRepository.save(department);
    }

    public Specialization persistSpecialization(AcademicDepartment department) {
        return persistSpecialization(uniqueName("Specialization"), department);
    }

    public Specialization persistSpecialization(String title, AcademicDepartment department) {
        Specialization specialization = new Specialization();
        specialization.setTitle(title);
        specialization.setCode(uniqueName("SP"));
        specialization.setDepartment(department);
        return specializationRepository.save(specialization);
    }

    public Classroom persistClassroom() {
        return persistClassroom(uniqueName("Room"));
    }

    public Classroom persistClassroom(String name) {
        Classroom classroom = new Classroom();
        classroom.setName(name);
        return classroomRepository.save(classroom);
    }

    public Subject persistSubject(AcademicDepartment department, List<Teacher> teachers) {
        return persistSubject(uniqueName("Subject"), department, teachers);
    }

    public Subject persistSubject(String title, AcademicDepartment department, List<Teacher> teachers) {
        Subject subject = new Subject();
        subject.setTitle(title);
        subject.setDepartment(department);
        subjectRepository.save(subject);

        teachers.forEach(teacher -> teacher.getSubjects().add(subject));
        teacherRepository.saveAll(teachers);

        return subject;
    }

    public Group persistGroup(Specialization specialization) {
        Group group = new Group();
        group.setSpecialization(specialization);
        group.setName(uniqueName("Group"));
        LocalDate startDate = LocalDate.now();
        group.setStartDate(startDate);
        group.setYearOfUniv(new YearOfUniv(startDate, 6));
        return groupRepository.save(group);
    }

    public Teacher persistTeacher(AcademicDepartment department) {
        Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.TEACHER));

        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName(uniqueName("Doe"));
        teacher.setEmail(uniqueName("teacher") + "@example.com");
        teacher.setPassword("Password123!");
        teacher.setRoles(roles);
        teacher.setDepartment(department);
        return teacherRepository.save(teacher);
    }
}
