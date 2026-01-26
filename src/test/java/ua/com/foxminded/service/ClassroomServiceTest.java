package ua.com.foxminded.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.BootTest;
import ua.com.foxminded.dto.ClassroomDto;
import ua.com.foxminded.entity.Classroom;

@Testcontainers
@Transactional
@SpringBootTest(classes = { BootTest.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ClassroomServiceTest {

    @Autowired
    private ClassroomService service;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        ClassroomDto classroom = new ClassroomDto();
        classroom.setName("2-A");
        Classroom savedClassroom = service.addOne(classroom);

        assertNotNull(savedClassroom.getId());
        assertEquals("2-A", savedClassroom.getName());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        ClassroomDto classroom = new ClassroomDto();
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
    void findByTitle_shouldReturnClassroom_whenExists() {

        ClassroomDto classroom = new ClassroomDto();
        classroom.setName("2-A");
        service.addOne(classroom);

        Optional<Classroom> result = service.findByTitle("2-A");

        assertTrue(result.isPresent());
        assertEquals("2-A", result.get().getName());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        ClassroomDto classroom = new ClassroomDto();
        classroom.setName("2-A");
        service.addOne(classroom);

        ClassroomDto classroom2 = new ClassroomDto();
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

        ClassroomDto classroom = new ClassroomDto();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);

        ClassroomDto updated = new ClassroomDto();
        updated.setName("3-A");
        Classroom classroomUpdated = service.update(updated, classroomSaved.getId());

        assertEquals(classroomSaved.getId(), classroomUpdated.getId());
        assertEquals("3-A", classroomUpdated.getName());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        ClassroomDto classroom = new ClassroomDto();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);

        ClassroomDto updated = new ClassroomDto();
        updated.setName(null);
        Classroom classroomUpdated = service.update(updated, classroomSaved.getId());

        assertEquals("2-A", classroomUpdated.getName());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        ClassroomDto classroom = new ClassroomDto();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);
        int id = classroomSaved.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
