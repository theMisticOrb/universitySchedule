package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.FacultyRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"test"})
class TeacherServiceTest {

    @Autowired
    private TeacherService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private TeacherCreateUpdateDto teacher = new TeacherCreateUpdateDto();

    @BeforeEach
    void setUp() {

        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.TEACHER);
        
        teacher.setDepartmentCode(csDepartment.getCode());
        teacher.setFirstName("Jhon");
        teacher.setLastName("Devis");
        teacher.setEmail("Devis@university.com");
        teacher.setPassword("JhonDevis1234");
        teacher.setRoles(roles);
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Teacher firstTeacher = service.addOne(teacher);

        assertNotNull(firstTeacher.getId());
        assertEquals("Jhon", firstTeacher.getFirstName());
        assertEquals("Devis", firstTeacher.getLastName());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Teacher saved = service.addOne(teacher);
        Teacher found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Teacher not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Teacher> allTeachers = service.findAll();

        assertNotNull(allTeachers);

    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Teacher firstTeacher = service.addOne(teacher);

        TeacherCreateUpdateDto firstTeacherUpdated = new TeacherCreateUpdateDto();
        firstTeacherUpdated.setFirstName("Gibbs");
        Teacher result = service.update(firstTeacherUpdated, firstTeacher.getId());

        assertEquals(firstTeacher.getId(), result.getId());
        assertEquals("Gibbs", result.getFirstName());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Teacher firstTeacher = service.addOne(teacher);

        TeacherCreateUpdateDto firstTeacherUpdated = new TeacherCreateUpdateDto();
        firstTeacherUpdated.setFirstName(null);
        Teacher result = service.update(firstTeacherUpdated, firstTeacher.getId());

        assertEquals(firstTeacher.getId(), result.getId());
        assertEquals("Jhon", result.getFirstName());

    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Teacher firstTeacher = service.addOne(teacher);
        int id = firstTeacher.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
