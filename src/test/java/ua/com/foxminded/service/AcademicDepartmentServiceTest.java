package ua.com.foxminded.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

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
import ua.com.foxminded.sdjpa.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AcademicDepartmentServiceTest {

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
    private AcademicDepartmentService service;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(savedFaculty);

        AcademicDepartment newDepartment = service.addOne(computerScience);

        assertNotNull(newDepartment.getId());
        assertEquals("Computer Science", newDepartment.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Multimedia");
        computerScience.setFaculty(savedFaculty);

        AcademicDepartment saved = service.addOne(computerScience);

        AcademicDepartment found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Multimedia", found.getTitle());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Academic Department not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(savedFaculty);
        service.addOne(computerScience);

        AcademicDepartment multimedia = new AcademicDepartment();
        multimedia.setTitle("Multimedia");
        multimedia.setFaculty(savedFaculty);
        service.addOne(multimedia);

        List<AcademicDepartment> allDepartments = service.findAll();

        assertEquals(2, allDepartments.size());

        List<String> titles = allDepartments.stream().map(AcademicDepartment::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("Computer Science"));
        assertTrue(titles.contains("Multimedia"));
    }

    @Test
    void findAll_shouldGetEmptyList_whenCalledIfNoEntities() {
        List<AcademicDepartment> allDepartments = service.findAll();
        assertTrue(allDepartments.isEmpty());
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        Faculty economy = new Faculty();
        economy.setTitle("Economy");
        Faculty economyFaculty = facultyRepository.save(economy);
        

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineegingFaculty);
        service.addOne(computerScience);

        computerScience.setFaculty(economyFaculty);
        AcademicDepartment computerScienceUpdated = service.update(computerScience);

        assertEquals(computerScience.getId(), computerScienceUpdated.getId());
        assertEquals("Computer Science", computerScienceUpdated.getTitle());
        assertEquals(economy.getTitle(), computerScienceUpdated.getFaculty().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineegingFaculty);
        AcademicDepartment department = service.addOne(computerScience);

        AcademicDepartment computerScienceUpdated = new AcademicDepartment();
        computerScienceUpdated.setId(department.getId());
        computerScienceUpdated.setTitle(null);
        AcademicDepartment result = service.update(computerScienceUpdated);

        assertEquals("Computer Science", result.getTitle());
        assertEquals(engineeging.getTitle(), result.getFaculty().getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineegingFaculty);
        AcademicDepartment department = service.addOne(computerScience);

        int id = department.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
