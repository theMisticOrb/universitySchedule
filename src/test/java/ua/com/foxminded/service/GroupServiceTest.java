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
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;
import ua.com.foxminded.sdjpa.SpecializationRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GroupServiceTest {

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
    private GroupService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;
    @Autowired
    private SpecializationRepository specializationRepository;

    private Group group = new Group();

    @BeforeEach
    void setUp() {
        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        Specialization specialization = new Specialization();
        specialization.setDepartment(csDepartment);
        specialization.setTitle("Security");
        Specialization security = specializationRepository.save(specialization);

        group.setFaculty(engineeringFaculty);
        group.setDepartment(csDepartment);
        group.setSpecialization(security);

    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Group firstGroup = service.addOne(group);

        assertNotNull(firstGroup.getId());
        assertEquals(1, firstGroup.getYearOfUniv().getCurentYearInUniv());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Group saved = service.addOne(group);
        Group found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Group not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Group> allGroups = service.findAll();

        assertNotNull(allGroups);

    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Group firstGroup = service.addOne(group);

        Faculty faculty = new Faculty();
        faculty.setTitle("Math");
        Faculty mathFaculty = facultyRepository.save(faculty);

        Group firstGroupUpdated = new Group();
        firstGroupUpdated.setId(firstGroup.getId());
        firstGroupUpdated.setFaculty(mathFaculty);
        Group result = service.update(firstGroupUpdated);

        assertEquals(firstGroup.getId(), result.getId());
        assertEquals(mathFaculty.getId(), result.getFaculty().getId());
        assertEquals(mathFaculty.getTitle(), result.getFaculty().getTitle());

    }
    
    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Group firstGroup = service.addOne(group);
        
        Group computerScienceUpdated = new Group();
        computerScienceUpdated.setId(firstGroup.getId());
        computerScienceUpdated.setFaculty(null);
        Group result = service.update(computerScienceUpdated);

        assertEquals("Engineering", result.getFaculty().getTitle());
    }
    
    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Group firstGroup = service.addOne(group);
        int id = firstGroup.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
