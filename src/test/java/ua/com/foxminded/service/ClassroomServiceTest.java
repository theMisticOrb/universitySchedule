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
import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"test"})
class ClassroomServiceTest {

    @Autowired
    private ClassroomService service;

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
        classroom.setName("2-A");
        Classroom savedClassroom = service.addOne(classroom);

        assertNotNull(savedClassroom.getId());
        assertEquals("2-A", savedClassroom.getName());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
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

        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
        classroom.setName("2-A");
        service.addOne(classroom);

        Classroom result = service.findByName("2-A");

        assertNotNull(result);
        assertEquals("2-A", result.getName());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
        classroom.setName("2-A");
        service.addOne(classroom);

        ClassroomCreateUpdateDto classroom2 = new ClassroomCreateUpdateDto();
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

        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);

        ClassroomCreateUpdateDto updated = new ClassroomCreateUpdateDto();
        updated.setName("3-A");
        Classroom classroomUpdated = service.update(updated, classroomSaved.getId());

        assertEquals(classroomSaved.getId(), classroomUpdated.getId());
        assertEquals("3-A", classroomUpdated.getName());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {
        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);

        ClassroomCreateUpdateDto updated = new ClassroomCreateUpdateDto();
        updated.setName(null);
        Classroom classroomUpdated = service.update(updated, classroomSaved.getId());

        assertEquals("2-A", classroomUpdated.getName());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        ClassroomCreateUpdateDto classroom = new ClassroomCreateUpdateDto();
        classroom.setName("2-A");
        Classroom classroomSaved = service.addOne(classroom);
        int id = classroomSaved.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
