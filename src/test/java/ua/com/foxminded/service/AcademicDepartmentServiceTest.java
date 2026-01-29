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
import ua.com.foxminded.dto.AcademicDepartmentDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AcademicDepartmentServiceTest {

    @Autowired
    private AcademicDepartmentService service;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentDto computerScience = new AcademicDepartmentDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyId(savedFaculty.getId());

        AcademicDepartment newDepartment = service.addOne(computerScience);

        assertNotNull(newDepartment.getId());
        assertEquals("Computer Science", newDepartment.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentDto multimedia = new AcademicDepartmentDto();
        multimedia.setTitle("Multimedia");
        multimedia.setFacultyId(savedFaculty.getId());

        AcademicDepartment saved = service.addOne(multimedia);

        AcademicDepartment found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Multimedia", found.getTitle());
    }

    @Test
    void findByTitle_shouldReturnDepartment_whenExists() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentDto multimedia = new AcademicDepartmentDto();
        multimedia.setTitle("Multimedia");
        multimedia.setFacultyId(savedFaculty.getId());

        service.addOne(multimedia);

        AcademicDepartment result = service.findByTitle("Multimedia");

        assertNotNull(result);
        assertEquals("Multimedia", result.getTitle());
        assertEquals(savedFaculty.getId(), result.getFaculty().getId());
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

        AcademicDepartmentDto computerScience = new AcademicDepartmentDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyId(savedFaculty.getId());
        service.addOne(computerScience);

        AcademicDepartmentDto multimedia = new AcademicDepartmentDto();
        multimedia.setTitle("Multimedia");
        multimedia.setFacultyId(savedFaculty.getId());
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

        AcademicDepartmentDto computerScience = new AcademicDepartmentDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyId(engineegingFaculty.getId());
        AcademicDepartment department = service.addOne(computerScience);

        AcademicDepartmentDto computerScienceUpdated = new AcademicDepartmentDto();
        computerScienceUpdated.setFacultyId(economyFaculty.getId());
        AcademicDepartment result = service.update(computerScienceUpdated, department.getId());

        assertEquals(department.getId(), result.getId());
        assertEquals("Computer Science", result.getTitle());
        assertEquals(economy.getTitle(), result.getFaculty().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        AcademicDepartmentDto computerScience = new AcademicDepartmentDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyId(engineegingFaculty.getId());
        AcademicDepartment department = service.addOne(computerScience);

        AcademicDepartmentDto computerScienceUpdated = new AcademicDepartmentDto();
        computerScienceUpdated.setFacultyId(engineegingFaculty.getId());
        computerScience.setTitle(null);
        AcademicDepartment result = service.update(computerScienceUpdated, department.getId());

        assertEquals("Computer Science", result.getTitle());
        assertEquals(engineeging.getTitle(), result.getFaculty().getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Faculty engineeging = new Faculty();
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        AcademicDepartmentDto computerScience = new AcademicDepartmentDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyId(engineegingFaculty.getId());
        AcademicDepartment department = service.addOne(computerScience);

        int id = department.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
