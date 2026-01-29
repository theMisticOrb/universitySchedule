package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.dto.SpecializationDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SpecializationServiceTest {

    @Autowired
    private SpecializationService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private SpecializationDto specialization = new SpecializationDto();

    @BeforeEach
    void setUp() {

        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        specialization.setDepartmentId(csDepartment.getId());
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
    void findByTitle_shouldReturnSpecialization_whenExists() {
        Specialization saved = service.addOne(specialization);

        Optional<Specialization> result = service.findByTitle("Security");

        assertTrue(result.isPresent());
        assertEquals("Security", result.get().getTitle());
        assertEquals(saved.getId(), result.get().getId());
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

        SpecializationDto securitySpecialUpdated = new SpecializationDto();
        securitySpecialUpdated.setDepartmentId(csDepartment.getId());
        Specialization result = service.update(securitySpecialUpdated, securitySpecial.getId());

        assertEquals(securitySpecial.getId(), result.getId());
        assertEquals(mathFaculty.getId(), result.getDepartment().getFaculty().getId());
        assertEquals(csDepartment.getTitle(), result.getDepartment().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Specialization securitySpecial = service.addOne(specialization);

        SpecializationDto securitySpecialUpdated = new SpecializationDto();
        securitySpecialUpdated.setDepartmentId(null);
        Specialization result = service.update(securitySpecialUpdated, securitySpecial.getId());

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
