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
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClassroomServiceTest {

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
    private ClassroomService service;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Classroom classroom = new Classroom();
        classroom.setName("2-A");
        Classroom savedClassroom = service.addOne(classroom);

        assertNotNull(savedClassroom.getId());
        assertEquals("2-A", savedClassroom.getName());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Classroom classroom = new Classroom();
        classroom.setName("2-A");
        Classroom savedClassroom = service.addOne(classroom);

        Classroom foundClassroom = service.findById(savedClassroom.getId());

        assertNotNull(foundClassroom);
        assertEquals(savedClassroom.getId(), foundClassroom.getId());
        assertEquals("2-A", foundClassroom.getName());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Classroom not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        Classroom classroom = new Classroom();
        classroom.setName("2-A");
        service.addOne(classroom);

        Classroom classroom2 = new Classroom();
        classroom2.setName("3-A");
        service.addOne(classroom2);

        List<Classroom> allClassrooms = service.findAll();

        assertEquals(2, allClassrooms.size());

        List<String> titles = allClassrooms.stream().map(Classroom::getName).collect(Collectors.toList());

        assertTrue(titles.contains("2-A"));
        assertTrue(titles.contains("3-A"));
    }

    @Test
    void findAll_shouldGetEmptyList_whenCalledIfNoEntities() {
        List<Classroom> allClassrooms = service.findAll();
        assertTrue(allClassrooms.isEmpty());
    }
    
    @Test
    void update_shouldUpdateEntity_whenCalled() {
//
//        AcademicDepartment computerScience = new AcademicDepartment();
//        computerScience.setTitle("Computer Science");
//        computerScience.setFaculty(engineegingFaculty);
//        AcademicDepartment department = service.addOne(computerScience);
//
//        AcademicDepartment computerScienceUpdated = new AcademicDepartment();
//        computerScienceUpdated.setId(department.getId());
//        computerScienceUpdated.setFaculty(economyFaculty);
//        AcademicDepartment result = service.update(computerScienceUpdated);
        
        Classroom classroom = new Classroom();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);
        
        classroom.setName("3-A");
        Classroom classroomUpdated = service.update(classroom);

        assertEquals(classroomSaved.getId(), classroomUpdated.getId());
        assertEquals("3-A", classroomUpdated.getName());

    }
    
    
}
