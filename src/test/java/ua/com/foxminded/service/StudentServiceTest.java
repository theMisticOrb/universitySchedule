package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.suppport.dto.StudentDtoTestBuilder;

class StudentServiceTest extends AbstractFullCrudServiceTest<Student, StudentCreateUpdateDto, Integer> {

    @Autowired
    private StudentService service;

    @Override
    protected FullCrudService<Student, StudentCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected StudentCreateUpdateDto createValidDto() {
        Group group = persistValidGroup();
        return StudentDtoTestBuilder.aStudent().withGroupName(group.getName()).build();
    }

    @Override
    protected Integer getId(Student entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    private Group persistValidGroup() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Specialization specialization = testDataFactory.persistSpecialization(department);
        return testDataFactory.persistGroup(specialization);
    }

    @Test
    void addOne_shouldThrowException_whenRoleIsNotStudent() {
        Group group = persistValidGroup();
        StudentCreateUpdateDto dto = StudentDtoTestBuilder.aStudent().withGroupName(group.getName())
                .withRoles(Collections.singleton(UserRole.TEACHER)).build();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.addOne(dto));

        assertEquals("User must have STUDENT role", ex.getMessage());
    }

    @Test
    void findByFullName_shouldReturnStudent_whenExists() {
        StudentCreateUpdateDto dto = createValidDto();
        Student saved = service.addOne(dto);

        Student result = service.findByFullName(dto.getLastName(), dto.getFirstName(), dto.getMiddleName());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void findByFullName_shouldThrowException_whenNotFound() {
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByFullName("Nobody", "Nobody", null));

        assertEquals("Student not found: Nobody Nobody null", ex.getMessage());
    }

    @Test
    void findByEmail_shouldReturnStudent_whenExists() {
        StudentCreateUpdateDto dto = createValidDto();
        Student saved = service.addOne(dto);

        Student result = service.findByEmail(dto.getEmail());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void findByEmail_shouldThrowException_whenNotFound() {
        String nonexistentEmail = "nobody@example.com";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByEmail(nonexistentEmail));

        assertEquals("Student are not found by this email: " + nonexistentEmail, ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Student not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void update_shouldChangeGroup_whenGroupNameProvided() {
        Student saved = getService().addOne(createValidDto());
        Group newGroup = persistValidGroup();

        StudentCreateUpdateDto updateDto = new StudentCreateUpdateDto();
        updateDto.setGroupName(newGroup.getName());

        Student result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals(newGroup.getId(), result.getGroup().getId());
    }

    @Test
    void update_shouldNotOverwriteFields_whenFieldIsNull() {
        StudentCreateUpdateDto dto = StudentDtoTestBuilder.aStudent().withGroupName(persistValidGroup().getName())
                .withFirstName("Jack").build();
        Student saved = service.addOne(dto);

        StudentCreateUpdateDto updateDto = new StudentCreateUpdateDto();
        updateDto.setFirstName(null);

        Student result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("Jack", result.getFirstName());
    }

    @Test
    void changeStudentGroup_shouldUpdateGroup_whenCalled() {
        Student saved = getService().addOne(createValidDto());
        Group newGroup = persistValidGroup();

        service.changeStudentGroup(saved.getId(), newGroup.getId());

        Student result = service.findById(saved.getId());
        assertEquals(newGroup.getId(), result.getGroup().getId());
    }

    @Test
    void addAll_shouldSaveAllProvidedStudents() {
        Group group = persistValidGroup();

        StudentCreateUpdateDto first = StudentDtoTestBuilder.aStudent().withGroupName(group.getName())
                .withFirstName("Jack").build();
        StudentCreateUpdateDto second = StudentDtoTestBuilder.aStudent().withGroupName(group.getName())
                .withFirstName("Gibbs").build();

        List<Student> result = service.addAll(Arrays.asList(first, second));

        assertEquals(2, result.size());
    }

    @Test
    void count_shouldReturnNumberOfStudents() {
        service.addOne(createValidDto());
        service.addOne(createValidDto());

        assertEquals(2, service.count());
    }

    @Test
    void count_shouldReturnZero_whenNoStudentsExist() {
        assertEquals(0, service.count());
    }
}