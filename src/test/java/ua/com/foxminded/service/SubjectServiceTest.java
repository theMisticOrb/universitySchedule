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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.BootTest;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;
import ua.com.foxminded.sdjpa.TeacherRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SubjectServiceTest {

    private static String dbTestName = "univ_schedule_app";

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName(dbTestName).withUsername("test").withPassword("1234");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    
    @Autowired
    private SubjectService service;
    
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;
    
    private Subject subject = new Subject();
    
    @BeforeEach
    void setUp() {
        
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);
        
        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);
        
        Teacher teacher = new Teacher();
        teacher.setFirstName("Don");
        teacher.setMiddleName("Bon");
        teacher.setLastName("Jon");
        teacher.setDepartment(csDepartment);
        Teacher jonTeacher = teacherRepository.save(teacher);
        
        subject.setTeacher(jonTeacher);
        subject.setTitle("Math");
        
    }
    
    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Subject firstSubject = service.addOne(subject);

        assertNotNull(firstSubject.getId());
        assertEquals("Math", firstSubject.getTitle());
    }
    
    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Subject saved = service.addOne(subject);
        Subject found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Subject not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Subject> allSubjects = service.findAll();

        assertNotNull(allSubjects);

    }
    
    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Subject firstSubject = service.addOne(subject);

        Subject firstSubjectUpdated = new Subject();
        firstSubjectUpdated.setId(firstSubject.getId());
        firstSubjectUpdated.setTitle("History");
        Subject result = service.update(firstSubjectUpdated);

        assertEquals(firstSubject.getId(), result.getId());
        assertEquals("History", result.getTitle());

    }
    
    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Subject firstSubject = service.addOne(subject);

        Subject firstSubjectUpdated = new Subject();
        firstSubjectUpdated.setId(firstSubject.getId());
        firstSubjectUpdated.setTitle(null);
        Subject result = service.update(firstSubjectUpdated);

        assertEquals(firstSubject.getId(), result.getId());
        assertEquals("Math", result.getTitle());

    }
    
    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Subject firstSubject = service.addOne(subject);
        int id = firstSubject.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
