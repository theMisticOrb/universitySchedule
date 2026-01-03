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
import ua.com.foxminded.entity.Faculty;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FacultyServiceTest {

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
    private FacultyService service;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {
        
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = service.addOne(faculty);

        assertNotNull(savedFaculty.getId());
        assertEquals("Engineering", savedFaculty.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty saved = service.addOne(faculty);

        Faculty found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Engineering", found.getTitle());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Faculty not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {
        Faculty facultyEngineering = new Faculty();
        facultyEngineering.setTitle("Engineering");
        service.addOne(facultyEngineering);

        Faculty facultyEconomy = new Faculty();
        facultyEconomy.setTitle("Economy");
        service.addOne(facultyEconomy);
        List<Faculty> allFaculties = service.findAll();

        assertEquals(2, allFaculties.size());

        List<String> titles = allFaculties.stream().map(Faculty::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("Economy"));
        assertTrue(titles.contains("Engineering"));
    }

    @Test
    void findAll_shouldGetEmptyList_whenCalledIfNoEntities() {
        List<Faculty> allFaculties = service.findAll();
        assertTrue(allFaculties.isEmpty());
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = service.addOne(engineeging);

        Faculty economy = new Faculty();
        economy.setId(engineeging.getId());
        economy.setTitle("Economy");
        Faculty economyFacultyUpdated = service.update(economy);

        assertEquals(engineegingFaculty.getId(), economyFacultyUpdated.getId());
        assertEquals("Economy", economyFacultyUpdated.getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = service.addOne(engineeging);

        Faculty economy = new Faculty();
        economy.setId(engineeging.getId());
        economy.setTitle(null);
        Faculty economyFacultyUpdated = service.update(engineeging);
        
        assertEquals(engineegingFaculty.getId(), economyFacultyUpdated.getId());
        assertEquals("Engineering", economyFacultyUpdated.getTitle());
    }
    
    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = service.addOne(engineeging);

        int id = engineegingFaculty.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }
}
