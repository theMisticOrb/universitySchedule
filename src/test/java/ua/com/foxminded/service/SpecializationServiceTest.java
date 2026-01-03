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
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SpecializationServiceTest {

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
    private SpecializationService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private Specialization specialization = new Specialization();

    @BeforeEach
    void setUp() {

        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        specialization.setDepartment(csDepartment);
        specialization.setTitle("Security");
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Specialization securitySpecial = service.addOne(specialization);

        assertNotNull(securitySpecial.getId());
        assertEquals("Security", securitySpecial.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Specialization saved = service.addOne(specialization);

        Specialization found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Specialization not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Specialization> allSpecializations = service.findAll();

        assertNotNull(allSpecializations);

    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {

        Specialization securitySpecial = service.addOne(specialization);

        Faculty faculty = new Faculty();
        faculty.setTitle("Math");
        Faculty mathFaculty = facultyRepository.save(faculty);

        AcademicDepartment analysis = new AcademicDepartment();
        analysis.setTitle("Mathematical analysis");
        analysis.setFaculty(mathFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(analysis);

        Specialization securitySpecialUpdated = new Specialization();
        securitySpecialUpdated.setId(securitySpecial.getId());
        securitySpecialUpdated.setDepartment(csDepartment);
        Specialization result = service.update(securitySpecialUpdated);

        assertEquals(securitySpecial.getId(), result.getId());
        assertEquals(mathFaculty.getId(), result.getDepartment().getFaculty().getId());
        assertEquals(csDepartment.getTitle(), result.getDepartment().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Specialization securitySpecial = service.addOne(specialization);

        Specialization securitySpecialUpdated = new Specialization();
        securitySpecialUpdated.setId(securitySpecial.getId());
        securitySpecialUpdated.setDepartment(null);
        Specialization result = service.update(securitySpecialUpdated);

        assertEquals("Computer Science", result.getDepartment().getTitle());
    }
    
    
    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        
        Specialization securitySpecial = service.addOne(specialization);
        int id = securitySpecial.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
