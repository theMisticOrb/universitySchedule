package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
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
import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.repository.TeacherRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class SubjectServiceTest {

    @Autowired
    private SubjectService service;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;

    private SubjectCreateUpdateDto subject = new SubjectCreateUpdateDto();

    @BeforeEach
    void setUp() {

        Faculty faculty = new Faculty();
        faculty.setTitle("Engineering");
        faculty.setCode("EN");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setCode("CS");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);
        
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.TEACHER);
        
        Teacher teacher = new Teacher();
        teacher.setFirstName("Don");
        teacher.setMiddleName("Bon");
        teacher.setLastName("Jon");
        teacher.setDepartment(csDepartment);
        teacher.setEmail("Jon@ua.com");
        teacher.setPassword("Jon1234");
        teacher.setRoles(roles);
        Teacher jonTeacher = teacherRepository.save(teacher);
        
        List<String> teacherEmails = new ArrayList<>();
        teacherEmails.add(jonTeacher.getEmail());

        subject.setTeacherEmails(teacherEmails);
        subject.setTitle("Math");
        subject.setDepartmentCode(csDepartment.getCode());

    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Subject firstSubject = service.addOne(subject);

        assertNotNull(firstSubject.getId());
        assertEquals("Math", firstSubject.getTitle());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Subject saved = service.addOne(subject);
        Subject found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Subject not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Subject> allSubjects = service.findAll();

        assertNotNull(allSubjects);

    }

    @Test
    void findByTitle_shouldReturnStudent_whenExists() {
        Subject saved = service.addOne(subject);

        Subject result = service.findByTitle("Math");

        assertNotNull(result);
        assertEquals("Math", result.getTitle());
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Subject firstSubject = service.addOne(subject);

        SubjectCreateUpdateDto firstSubjectUpdated = new SubjectCreateUpdateDto();
        firstSubjectUpdated.setTitle("History");
        Subject result = service.update(firstSubjectUpdated, firstSubject.getId());

        assertEquals(firstSubject.getId(), result.getId());
        assertEquals("History", result.getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        Subject firstSubject = service.addOne(subject);

        SubjectCreateUpdateDto firstSubjectUpdated = new SubjectCreateUpdateDto();
        firstSubjectUpdated.setTitle(null);
        Subject result = service.update(firstSubjectUpdated, firstSubject.getId());

        assertEquals(firstSubject.getId(), result.getId());
        assertEquals("Math", result.getTitle());

    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Subject firstSubject = service.addOne(subject);
        int id = firstSubject.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
