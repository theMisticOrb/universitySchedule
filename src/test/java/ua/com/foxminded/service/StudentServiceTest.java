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

import ua.com.foxminded.BootTest;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;
import ua.com.foxminded.sdjpa.FacultyRepository;
import ua.com.foxminded.sdjpa.SpecializationRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class StudentServiceTest {

    @Autowired
    private StudentService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private GroupService groupService;

    private Student student = new Student();

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

        Group group = new Group();
        group.setFaculty(engineeringFaculty);
        group.setDepartment(csDepartment);
        group.setSpecialization(security);
        Group firstGroup = groupService.addOne(group);

        student.setFirstName("Jack");
        student.setLastName("Sparrow");
        student.setGroup(firstGroup);
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Student firstStudent = service.addOne(student);

        assertNotNull(firstStudent.getId());
        assertEquals("Jack", firstStudent.getFirstName());
        assertEquals("Sparrow", firstStudent.getLastName());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Student saved = service.addOne(student);
        Student found = service.findById(saved.getId());

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

        List<Student> allStudents = service.findAll();

        assertNotNull(allStudents);

    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Student firstStudent = service.addOne(student);

        Student firstStudentUpdated = new Student();
        firstStudentUpdated.setId(firstStudent.getId());
        firstStudentUpdated.setFirstName("Gibbs");
        Student result = service.update(firstStudentUpdated);

        assertEquals(firstStudent.getId(), result.getId());
        assertEquals("Gibbs", result.getFirstName());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Student firstStudent = service.addOne(student);

        Student firstStudentUpdated = new Student();
        firstStudentUpdated.setId(firstStudent.getId());
        firstStudentUpdated.setFirstName(null);
        Student result = service.update(firstStudentUpdated);

        assertEquals(firstStudent.getId(), result.getId());
        assertEquals("Jack", result.getFirstName());

    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Student firstStudent = service.addOne(student);
        int id = firstStudent.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
