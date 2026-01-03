package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.*;

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
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeacherServiceTest {

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
    private TeacherService service;
    
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private Teacher teacher = new Teacher();
    
    @BeforeEach
    void setUp() {
        
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);
        
        teacher.setDepartment(csDepartment);
        teacher.setFirstName("Jhon");
        teacher.setLastName("Devis");
    }
    
    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Teacher firstTeacher = service.addOne(teacher);

        assertNotNull(firstTeacher.getId());
        assertEquals("Jhon", firstTeacher.getFirstName());
        assertEquals("Devis", firstTeacher.getLastName());
    }
    
    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Teacher saved = service.addOne(teacher);

        Teacher found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }
    
    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Student not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Teacher> allTeachers = service.findAll();

        assertNotNull(allTeachers);

    }
    
    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Teacher firstTeacher = service.addOne(teacher);

        Teacher firstTeacherUpdated = new Teacher();
        firstTeacherUpdated.setId(firstTeacher.getId());
        firstTeacherUpdated.setFirstName("Gibbs");
        Teacher result = service.update(firstTeacherUpdated);

        assertEquals(firstTeacher.getId(), result.getId());
        assertEquals("Gibbs", result.getFirstName());

    }
    
    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Teacher firstTeacher = service.addOne(teacher);

        Teacher firstTeacherUpdated = new Teacher();
        firstTeacherUpdated.setId(firstTeacher.getId());
        firstTeacherUpdated.setFirstName(null);
        Teacher result = service.update(firstTeacherUpdated);

        assertEquals(firstTeacher.getId(), result.getId());
        assertEquals("Jhon", result.getFirstName());

    }
    
    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Teacher firstTeacher = service.addOne(teacher);
        int id = firstTeacher.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
