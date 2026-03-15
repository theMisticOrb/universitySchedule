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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.FacultyRepository;

@SpringBootTest(classes = { MainApp.class })
@Transactional
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class SpecializationServiceTest {

    @Autowired
    private SpecializationService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private SpecializationCreateUpdateDto specialization = new SpecializationCreateUpdateDto();

    @BeforeEach
    void setUp() {

        Faculty faculty = new Faculty();
        faculty.setCode("EN");
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.saveAndFlush(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setCode("CS");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.saveAndFlush(computerScience);

        specialization.setDepartmentCode(csDepartment.getCode());
        specialization.setCode("SEC");
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
        assertEquals("Specialization not found by id: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Specialization> allSpecializations = service.findAll();

        assertNotNull(allSpecializations);

    }

    @Test
    void findByTitle_shouldReturnSpecialization_whenExists() {
        Specialization saved = service.addOne(specialization);

        Specialization result = service.findByTitle("Security");

        assertNotNull(result);
        assertEquals("Security", result.getTitle());
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {

        Specialization securitySpecial = service.addOne(specialization);

        Faculty faculty = new Faculty();
        faculty.setTitle("Math");
        faculty.setCode("MT");
        Faculty mathFaculty = facultyRepository.save(faculty);

        AcademicDepartment analysis = new AcademicDepartment();
        analysis.setTitle("Mathematical analysis");
        analysis.setCode("MA");
        analysis.setFaculty(mathFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(analysis);

        SpecializationCreateUpdateDto securitySpecialUpdated = new SpecializationCreateUpdateDto();
        securitySpecialUpdated.setDepartmentCode(csDepartment.getCode());
        Specialization result = service.update(securitySpecialUpdated, securitySpecial.getId());

        assertEquals(securitySpecial.getId(), result.getId());
        assertEquals(mathFaculty.getId(), result.getDepartment().getFaculty().getId());
        assertEquals(csDepartment.getTitle(), result.getDepartment().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Specialization securitySpecial = service.addOne(specialization);

        SpecializationCreateUpdateDto securitySpecialUpdated = new SpecializationCreateUpdateDto();
        securitySpecialUpdated.setDepartmentCode(null);
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
