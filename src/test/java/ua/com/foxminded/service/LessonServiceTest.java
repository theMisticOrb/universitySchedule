package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.BootTest;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.ClassroomRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;
import ua.com.foxminded.sdjpa.SubjectRepository;
import ua.com.foxminded.sdjpa.TeacherRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class LessonServiceTest {

    @Autowired
    private LessonService service;

    @Autowired
    private ClassroomRepository clasroomRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private Lesson lesson = new Lesson();
    private Teacher teacher = new Teacher();

    @BeforeEach
    void setUp() {
        Classroom classrom = new Classroom();
        classrom.setName("2-A");
        Classroom secondAClassroom = clasroomRepository.save(classrom);

        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        teacher.setFirstName("Don");
        teacher.setMiddleName("Bon");
        teacher.setLastName("Jon");
        teacher.setDepartment(csDepartment);
        Teacher jonTeacher = teacherRepository.save(teacher);

        Subject subject = new Subject();
        subject.setTitle("Math");
        subject.setTeacher(jonTeacher);
        Subject math = subjectRepository.save(subject);

        lesson.setClassroom(secondAClassroom);
        lesson.setSubject(math);
        lesson.setWeekDays(WeekDay.FRIDAY);
        lesson.setClassHours(ClassHour.FIRST_CLASS);
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Lesson firstLesson = service.addOne(lesson);

        assertNotNull(firstLesson.getId());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Lesson saved = service.addOne(lesson);
        Lesson found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Lesson not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Lesson> allGroups = service.findAll();
        assertNotNull(allGroups);

    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Lesson firstLesson = service.addOne(lesson);

        Subject subject = new Subject();
        subject.setTitle("History");
        subject.setTeacher(teacher);
        Subject history = subjectRepository.save(subject);

        Lesson firstLessonUpdated = new Lesson();
        firstLessonUpdated.setId(firstLesson.getId());
        firstLessonUpdated.setSubject(history);
        Lesson result = service.update(firstLessonUpdated);

        assertEquals(firstLesson.getId(), result.getId());
        assertEquals(history.getId(), result.getSubject().getId());
        assertEquals(history.getTitle(), result.getSubject().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Lesson firstLesson = service.addOne(lesson);

        Lesson firstLessonUpdated = new Lesson();
        firstLessonUpdated.setId(firstLesson.getId());
        firstLessonUpdated.setSubject(null);
        Lesson result = service.update(firstLessonUpdated);

        assertEquals("Math", result.getSubject().getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Lesson firstLesson = service.addOne(lesson);
        int id = firstLesson.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }
}
