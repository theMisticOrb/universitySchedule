package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.entity.YearOfUniv;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.SpecializationRepository;
import ua.com.foxminded.repository.SubjectRepository;
import ua.com.foxminded.repository.TeacherRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    private SpecializationRepository specializationRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private LessonCreateUpdateDto lesson = new LessonCreateUpdateDto();
    private Teacher teacher = new Teacher();
    private AcademicDepartment computerScience = new AcademicDepartment();

    @BeforeEach
    void setUp() {
        Classroom classrom = new Classroom();
        classrom.setName("2-A");
        Classroom secondAClassroom = clasroomRepository.save(classrom);

        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        Specialization specialization = new Specialization();
        specialization.setCode("CS");
        specialization.setDepartment(computerScience);
        specialization.setTitle("IT");
        Specialization savedSpecialization = specializationRepository.save(specialization);

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.TEACHER);
        
        teacher.setFirstName("Don");
        teacher.setMiddleName("Bon");
        teacher.setLastName("Jon");
        teacher.setDepartment(csDepartment);
        teacher.setEmail("Jon@ua.com");
        teacher.setPassword("Jon1234");
        teacher.setRoles(roles);
        teacher.setId(1);
        Teacher jonTeacher = teacherRepository.save(teacher);

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(jonTeacher);
       
        
        Subject subject = new Subject();
        subject.setTitle("Math");
        subject.setTeachers(teachers);
        subject.setDepartment(computerScience);
        Subject math = subjectRepository.save(subject);

        Group group = new Group();
        group.setSpecialization(savedSpecialization);
        group.setName("1K");
        group.setStartDate(LocalDate.now());
        group.setYearOfUniv(new YearOfUniv(2, 6));
        Group savedGroup = groupRepository.save(group);

        lesson.setClassroomName(secondAClassroom.getName());
        lesson.setSubjectName(math.getTitle());
        lesson.setWeekDay(WeekDay.FRIDAY);
        lesson.setClassHour(ClassHour.FIRST_CLASS);
        lesson.setGroupName(savedGroup.getName());
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Lesson firstLesson = service.addOne(lesson, 1);

        assertNotNull(firstLesson.getId());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Lesson saved = service.addOne(lesson, 1);
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
    void findByTitle_shouldReturnLessonBySubjectName_whenExists() {
        Lesson saved = service.addOne(lesson, 1);

        List<Lesson> result = service.findBySubjectTitleAndWeekDay("Math", WeekDay.FRIDAY);

        assertNotNull(result);
        assertTrue(result.contains(saved));
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Lesson firstLesson = service.addOne(lesson, 1);
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
       
        Subject subject = new Subject();
        subject.setTitle("History");
        subject.setTeachers(teachers);
        subject.setDepartment(computerScience);
        Subject history = subjectRepository.save(subject);

        LessonCreateUpdateDto firstLessonUpdated = new LessonCreateUpdateDto();
        firstLessonUpdated.setSubjectName(history.getTitle());
        Lesson result = service.update(firstLessonUpdated, firstLesson.getId());

        assertEquals(firstLesson.getId(), result.getId());
        assertEquals(history.getId(), result.getSubject().getId());
        assertEquals(history.getTitle(), result.getSubject().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Lesson firstLesson = service.addOne(lesson, 1);

        LessonCreateUpdateDto firstLessonUpdated = new LessonCreateUpdateDto();
        firstLessonUpdated.setSubjectName(null);
        Lesson result = service.update(firstLessonUpdated, firstLesson.getId());

        assertEquals("Math", result.getSubject().getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Lesson firstLesson = service.addOne(lesson, 1);
        int id = firstLesson.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }
}
