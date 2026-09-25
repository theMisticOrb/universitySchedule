package ua.com.foxminded.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.suppport.dto.ClassroomDtoTestBuilder;

class ClassroomServiceTest extends AbstractFullCrudServiceTest<Classroom, ClassroomCreateUpdateDto, Integer> {
    @Autowired
    private ClassroomService service;

    @Override
    protected FullCrudService<Classroom, ClassroomCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected ClassroomCreateUpdateDto createValidDto() {
        return ClassroomDtoTestBuilder.aClassroom().build();
    }

    @Override
    protected Integer getId(Classroom entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    @Test
    void findByName_shouldReturnClassroom_whenExists() {
        ClassroomCreateUpdateDto dto = ClassroomDtoTestBuilder.aClassroom().build();
        service.addOne(dto);

        Classroom result = service.findByName(dto.getName());

        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void findByName_shouldThrowException_whenNotFound() {
        String nonexistentName = "Nonexistent Room";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByName(nonexistentName));

        assertEquals("Classroom not found: " + nonexistentName, ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Classroom not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void addOne_shouldThrowDuplicateEntityException_whenNameAlreadyExists() {
        ClassroomCreateUpdateDto dto = ClassroomDtoTestBuilder.aClassroom().withName("2-A").build();
        service.addOne(dto);

        ClassroomCreateUpdateDto duplicateDto = ClassroomDtoTestBuilder.aClassroom().withName("2-A").build();

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class, () -> service.addOne(duplicateDto));

        assertEquals("Classroom with title: 2-A alredy exists!", ex.getMessage());
    }

    @Test
    void fullUpdate_shouldChangeName_whenNameProvided() {
        ClassroomCreateUpdateDto dto = ClassroomDtoTestBuilder.aClassroom().withName("2-A").build();
        Classroom saved = service.addOne(dto);

        ClassroomCreateUpdateDto updateDto = new ClassroomCreateUpdateDto();
        updateDto.setName("3-A");

        Classroom result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals("3-A", result.getName());
    }

    @Test
    void fullUpdate_shouldNotOverwriteNullFields_whenFieldIsNull() {
        ClassroomCreateUpdateDto dto = ClassroomDtoTestBuilder.aClassroom().withName("2-A").build();
        Classroom saved = service.addOne(dto);

        ClassroomCreateUpdateDto updateDto = new ClassroomCreateUpdateDto();
        updateDto.setName(null);

        Classroom result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("2-A", result.getName());
    }

    @Test
    void createClassrooms_shouldSaveAllProvidedClassrooms() {
        ClassroomCreateUpdateDto first = ClassroomDtoTestBuilder.aClassroom().withName("2-A").build();
        ClassroomCreateUpdateDto second = ClassroomDtoTestBuilder.aClassroom().withName("3-A").build();

        service.createClassrooms(Arrays.asList(first, second));

        List<Classroom> allClassrooms = service.findAll();
        List<String> names = allClassrooms.stream().map(Classroom::getName).collect(Collectors.toList());

        assertTrue(names.contains("2-A"));
        assertTrue(names.contains("3-A"));
    }
}
