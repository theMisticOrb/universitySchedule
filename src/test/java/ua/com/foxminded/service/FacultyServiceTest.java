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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.entity.Faculty;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class FacultyServiceTest {

    @Autowired
    private FacultyService service;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        FacultyCreateUpdateDto faculty = new FacultyCreateUpdateDto();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = service.addOne(faculty);

        assertNotNull(savedFaculty.getId());
        assertEquals("Engineering", savedFaculty.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {
        FacultyCreateUpdateDto faculty = new FacultyCreateUpdateDto();
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
        assertEquals("Faculty not found by id: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {
        FacultyCreateUpdateDto facultyEngineering = new FacultyCreateUpdateDto();
        facultyEngineering.setTitle("Engineering");
        service.addOne(facultyEngineering);

        FacultyCreateUpdateDto facultyEconomy = new FacultyCreateUpdateDto();
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
    void findByTitle_shouldReturnFaculty_whenExists() {
        FacultyCreateUpdateDto faculty = new FacultyCreateUpdateDto();
        faculty.setTitle("Engineering");

        service.addOne(faculty);

        Faculty result = service.findByTitle("Engineering");

        assertNotNull(result);
        assertEquals("Engineering", result.getTitle());
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        FacultyCreateUpdateDto engineeging = new FacultyCreateUpdateDto();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = service.addOne(engineeging);

        FacultyCreateUpdateDto economy = new FacultyCreateUpdateDto();
        economy.setTitle("Economy");
        Faculty economyFacultyUpdated = service.update(economy, engineegingFaculty.getId());

        assertEquals(engineegingFaculty.getId(), economyFacultyUpdated.getId());
        assertEquals("Economy", economyFacultyUpdated.getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        FacultyCreateUpdateDto engineeging = new FacultyCreateUpdateDto();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = service.addOne(engineeging);

        FacultyCreateUpdateDto economy = new FacultyCreateUpdateDto();
        economy.setTitle(null);
        Faculty economyFacultyUpdated = service.update(engineeging, engineegingFaculty.getId());

        assertEquals(engineegingFaculty.getId(), economyFacultyUpdated.getId());
        assertEquals("Engineering", economyFacultyUpdated.getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        FacultyCreateUpdateDto engineeging = new FacultyCreateUpdateDto();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = service.addOne(engineeging);

        int id = engineegingFaculty.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }
}
