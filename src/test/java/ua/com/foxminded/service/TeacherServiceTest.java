package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.suppport.dto.TeacherDtoTestBuilder;

class TeacherServiceTest extends AbstractFullCrudServiceTest<Teacher, TeacherCreateUpdateDto, Integer> {

    @Autowired
    private TeacherService service;

    @Override
    protected FullCrudService<Teacher, TeacherCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected TeacherCreateUpdateDto createValidDto() {
        AcademicDepartment department = persistValidDepartment();
        return TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode()).build();
    }

    @Override
    protected Integer getId(Teacher entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    private AcademicDepartment persistValidDepartment() {
        Faculty faculty = testDataFactory.persistFaculty();
        return testDataFactory.persistDepartment(faculty);
    }

    @Test
    void addOne_shouldThrowException_whenRoleIsNotTeacher() {
        AcademicDepartment department = persistValidDepartment();
        TeacherCreateUpdateDto dto = TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode())
                .withRoles(Collections.singleton(UserRole.STUDENT)).build();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> service.addOne(dto));

        assertEquals("User must have TEACHER role", ex.getMessage());
    }

    @Test
    void findByEmail_shouldReturnTeacher_whenExists() {
        TeacherCreateUpdateDto dto = createValidDto();
        Teacher saved = service.addOne(dto);

        Teacher result = service.findByEmail(dto.getEmail());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void findByEmail_shouldThrowException_whenNotFound() {
        String nonexistentEmail = "nobody@example.com";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByEmail(nonexistentEmail));

        assertEquals("Teacher are not found by this email: " + nonexistentEmail, ex.getMessage());
    }

    @Test
    void findByFullName_shouldReturnTeacher_whenExists() {
        TeacherCreateUpdateDto dto = createValidDto();
        Teacher saved = service.addOne(dto);

        Teacher result = service.findByFullName(dto.getLastName(), dto.getFirstName(), dto.getMiddleName());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void findByFullName_shouldThrowException_whenNotFound() {
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByFullName("Nobody", "Nobody", null));

        assertEquals("Teacher not found: Nobody Nobody null", ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Teacher not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void update_shouldChangeDepartment_whenDepartmentCodeProvided() {
        Teacher saved = getService().addOne(createValidDto());
        AcademicDepartment newDepartment = persistValidDepartment();

        TeacherCreateUpdateDto updateDto = new TeacherCreateUpdateDto();
        updateDto.setDepartmentCode(newDepartment.getCode());

        Teacher result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals(newDepartment.getId(), result.getDepartment().getId());
    }

    @Test
    void update_shouldNotOverwriteFields_whenFieldIsNull() {
        TeacherCreateUpdateDto dto = TeacherDtoTestBuilder.aTeacher()
                .withDepartmentCode(persistValidDepartment().getCode()).withFirstName("Jhon").build();
        Teacher saved = service.addOne(dto);

        TeacherCreateUpdateDto updateDto = new TeacherCreateUpdateDto();
        updateDto.setFirstName(null);

        Teacher result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("Jhon", result.getFirstName());
    }

    @Test
    void changeTeacherDepartment_shouldUpdateDepartment_whenCalled() {
        Teacher saved = getService().addOne(createValidDto());
        AcademicDepartment newDepartment = persistValidDepartment();

        service.changeTeacherDepartment(saved.getId(), newDepartment.getId());

        Teacher result = service.findById(saved.getId());
        assertEquals(newDepartment.getId(), result.getDepartment().getId());
    }

    @Test
    void assignSubjects_shouldAddNewSubjectsToTeacher_whenCalled() {
        AcademicDepartment department = persistValidDepartment();
        Teacher saved = service
                .addOne(TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode()).build());
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(saved));
        service.assignSubjects(saved.getId(), Arrays.asList(subject.getTitle()));

        Teacher result = service.findById(saved.getId());
        assertTrue(result.getSubjects().contains(subject));
    }

    @Test
    void assignSubjects_shouldNotDuplicateAlreadyAssignedSubject() {
        AcademicDepartment department = persistValidDepartment();
        Teacher saved = service
                .addOne(TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode()).build());
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(saved));

        service.assignSubjects(saved.getId(), Arrays.asList(subject.getTitle()));

        Teacher result = service.findById(saved.getId());
        assertEquals(1, result.getSubjects().stream().filter(s -> s.equals(subject)).count());
    }

    @Test
    void assignSubjects_shouldThrowException_whenSubjectNotFound() {
        Teacher saved = getService().addOne(createValidDto());
        String nonexistentTitle = "Nonexistent Subject";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.assignSubjects(saved.getId(), Collections.singletonList(nonexistentTitle)));

        assertEquals("Subject not found: " + nonexistentTitle, ex.getMessage());
    }

    @Test
    void removeSubjectFromTeacher_shouldRemoveSubject_whenCalled() {
        AcademicDepartment department = persistValidDepartment();
        Teacher saved = service
                .addOne(TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode()).build());
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(saved));
        service.assignSubjects(saved.getId(), Arrays.asList(subject.getTitle()));

        service.removeSubjectFromTeacher(saved.getId(), subject.getTitle());

        Teacher result = service.findById(saved.getId());
        assertFalse(result.getSubjects().contains(subject));
    }

    @Test
    void removeSubjectFromTeacher_shouldThrowException_whenSubjectNotFound() {
        Teacher saved = getService().addOne(createValidDto());
        String nonexistentTitle = "Nonexistent Subject";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.removeSubjectFromTeacher(saved.getId(), nonexistentTitle));

        assertEquals("Subject not found: " + nonexistentTitle, ex.getMessage());
    }

    @Test
    void addAll_shouldSaveAllProvidedTeachers() {
        AcademicDepartment department = persistValidDepartment();

        TeacherCreateUpdateDto first = TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode())
                .withFirstName("Jhon").build();
        TeacherCreateUpdateDto second = TeacherDtoTestBuilder.aTeacher().withDepartmentCode(department.getCode())
                .withFirstName("Gibbs").build();

        List<Teacher> result = service.addAll(Arrays.asList(first, second));

        assertEquals(2, result.size());
    }

    @Test
    void count_shouldReturnNumberOfTeachers() {
        service.addOne(createValidDto());
        service.addOne(createValidDto());

        assertEquals(2, service.count());
    }

    @Test
    void count_shouldReturnZero_whenNoTeachersExist() {
        assertEquals(0, service.count());
    }
}