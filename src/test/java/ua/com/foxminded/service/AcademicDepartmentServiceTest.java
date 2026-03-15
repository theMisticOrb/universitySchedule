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
import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.repository.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class AcademicDepartmentServiceTest {

    @Autowired
    private AcademicDepartmentService service;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {
        Faculty faculty = new Faculty();
        faculty.setCode("EN");
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentCreateUpdateDto computerScience = new AcademicDepartmentCreateUpdateDto();
        computerScience.setTitle("Computer Science");
        computerScience.setCode("CS");
        computerScience.setFacultyCode(savedFaculty.getCode());

        AcademicDepartment newDepartment = service.addOne(computerScience);

        assertNotNull(newDepartment.getId());
        assertEquals("Computer Science", newDepartment.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {
        Faculty faculty = new Faculty();
        faculty.setCode("EN");
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentCreateUpdateDto multimedia = new AcademicDepartmentCreateUpdateDto();
        multimedia.setTitle("Multimedia");
        multimedia.setCode("MM");
        multimedia.setFacultyCode(savedFaculty.getCode());

        AcademicDepartment saved = service.addOne(multimedia);

        AcademicDepartment found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals("Multimedia", found.getTitle());
    }

    @Test
    void findByTitle_shouldReturnDepartment_whenExists() {
        Faculty faculty = new Faculty();
        faculty.setCode("EN");
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentCreateUpdateDto multimedia = new AcademicDepartmentCreateUpdateDto();
        multimedia.setTitle("Multimedia");
        multimedia.setCode("MM");
        multimedia.setFacultyCode(savedFaculty.getCode());

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
        assertEquals("Academic Department not found by id: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty savedFaculty = facultyRepository.save(faculty);

        AcademicDepartmentCreateUpdateDto computerScience = new AcademicDepartmentCreateUpdateDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyCode(savedFaculty.getCode());
        service.addOne(computerScience);

        AcademicDepartmentCreateUpdateDto multimedia = new AcademicDepartmentCreateUpdateDto();
        multimedia.setTitle("Multimedia");
        multimedia.setFacultyCode(savedFaculty.getCode());
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
        engineeging.setCode("EN");
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        Faculty economy = new Faculty();
        economy.setCode("EC");
        economy.setTitle("Economy");
        Faculty economyFaculty = facultyRepository.save(economy);

        AcademicDepartmentCreateUpdateDto computerScience = new AcademicDepartmentCreateUpdateDto();
        computerScience.setTitle("Computer Science");
        computerScience.setCode("CS");
        computerScience.setFacultyCode(engineegingFaculty.getCode());
        AcademicDepartment department = service.addOne(computerScience);

        AcademicDepartmentCreateUpdateDto computerScienceUpdated = new AcademicDepartmentCreateUpdateDto();
        computerScienceUpdated.setFacultyCode(economyFaculty.getCode());
        AcademicDepartment result = service.update(computerScienceUpdated, department.getId());

        assertEquals(department.getId(), result.getId());
        assertEquals("Computer Science", result.getTitle());
        assertEquals(economy.getTitle(), result.getFaculty().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Faculty engineeging = new Faculty();
        engineeging.setCode("EN");
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        AcademicDepartmentCreateUpdateDto computerScience = new AcademicDepartmentCreateUpdateDto();
        computerScience.setTitle("Computer Science");
        computerScience.setCode("CS");
        computerScience.setFacultyCode(engineegingFaculty.getCode());
        AcademicDepartment department = service.addOne(computerScience);

        AcademicDepartmentCreateUpdateDto computerScienceUpdated = new AcademicDepartmentCreateUpdateDto();
        computerScienceUpdated.setFacultyCode(engineegingFaculty.getCode());
        computerScience.setTitle(null);
        AcademicDepartment result = service.update(computerScienceUpdated, department.getId());

        assertEquals("Computer Science", result.getTitle());
        assertEquals(engineeging.getTitle(), result.getFaculty().getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Faculty engineeging = new Faculty();
        engineeging.setCode("EN");
        engineeging.setTitle("Engineering");
        Faculty engineegingFaculty = facultyRepository.save(engineeging);

        AcademicDepartmentCreateUpdateDto computerScience = new AcademicDepartmentCreateUpdateDto();
        computerScience.setTitle("Computer Science");
        computerScience.setFacultyCode(engineegingFaculty.getCode());
        AcademicDepartment department = service.addOne(computerScience);

        int id = department.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
