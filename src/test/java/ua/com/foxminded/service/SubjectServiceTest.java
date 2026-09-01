package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.suppport.dto.SubjectDtoTestBuilder;

class SubjectServiceTest extends AbstractFullCrudServiceTest<Subject, SubjectCreateUpdateDto, Integer> {

    @Autowired
    private SubjectService service;

    @Override
    protected FullCrudService<Subject, SubjectCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected SubjectCreateUpdateDto createValidDto() {
        AcademicDepartment department = persistValidDepartment();
        Teacher teacher = testDataFactory.persistTeacher(department);

        return SubjectDtoTestBuilder.aSubject().withDepartmentCode(department.getCode())
                .withTeacherEmails(Collections.singletonList(teacher.getEmail())).build();
    }

    @Override
    protected Integer getId(Subject entity) {
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
    void findByTitle_shouldReturnSubject_whenExists() {
        SubjectCreateUpdateDto dto = createValidDto();
        Subject saved = service.addOne(dto);

        Subject result = service.findByTitle(dto.getTitle());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void findByTitle_shouldThrowException_whenNotFound() {
        String nonexistentTitle = "Nonexistent Subject";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByTitle(nonexistentTitle));

        assertEquals("Subject not found: " + nonexistentTitle, ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Subject not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void addOne_shouldThrowDuplicateEntityException_whenTitleAlreadyExists() {
        AcademicDepartment department = persistValidDepartment();
        Teacher teacher = testDataFactory.persistTeacher(department);

        SubjectCreateUpdateDto dto = SubjectDtoTestBuilder.aSubject().withTitle("Math")
                .withDepartmentCode(department.getCode())
                .withTeacherEmails(Collections.singletonList(teacher.getEmail())).build();
        service.addOne(dto);

        SubjectCreateUpdateDto duplicateDto = SubjectDtoTestBuilder.aSubject().withTitle("Math")
                .withDepartmentCode(department.getCode())
                .withTeacherEmails(Collections.singletonList(teacher.getEmail())).build();

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class, () -> service.addOne(duplicateDto));

        assertEquals("Subject with name: Math alredy exists!", ex.getMessage());
    }

    @Test
    void update_shouldChangeTeachers_whenTeacherEmailsProvided() {
        AcademicDepartment department = persistValidDepartment();
        Teacher originalTeacher = testDataFactory.persistTeacher(department);

        SubjectCreateUpdateDto dto = SubjectDtoTestBuilder.aSubject().withDepartmentCode(department.getCode())
                .withTeacherEmails(Collections.singletonList(originalTeacher.getEmail())).build();
        Subject saved = service.addOne(dto);

        Teacher newTeacher = testDataFactory.persistTeacher(department);
        SubjectCreateUpdateDto updateDto = new SubjectCreateUpdateDto();
        updateDto.setTeacherEmails(Collections.singletonList(newTeacher.getEmail()));

        Subject result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals(1, result.getTeachers().size());
        assertEquals(newTeacher.getId(), result.getTeachers().get(0).getId());
    }

    @Test
    void update_shouldNotOverwriteTitle_whenTitleIsNull() {
        SubjectCreateUpdateDto dto = SubjectDtoTestBuilder.aSubject().withTitle("Math")
                .withDepartmentCode(persistValidDepartment().getCode()).build();
        Subject saved = service.addOne(dto);

        SubjectCreateUpdateDto updateDto = new SubjectCreateUpdateDto();
        updateDto.setTitle(null);

        Subject result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("Math", result.getTitle());
    }

    @Test
    void createSubjects_shouldSaveAllProvidedSubjects() {
        AcademicDepartment department = persistValidDepartment();
        Teacher teacher = testDataFactory.persistTeacher(department);

        SubjectCreateUpdateDto first = SubjectDtoTestBuilder.aSubject().withTitle("Math")
                .withDepartmentCode(department.getCode())
                .withTeacherEmails(Collections.singletonList(teacher.getEmail())).build();
        SubjectCreateUpdateDto second = SubjectDtoTestBuilder.aSubject().withTitle("History")
                .withDepartmentCode(department.getCode())
                .withTeacherEmails(Collections.singletonList(teacher.getEmail())).build();

        service.createSubjects(Arrays.asList(first, second));

        List<Subject> all = service.findAll();
        List<String> titles = all.stream().map(Subject::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("Math"));
        assertTrue(titles.contains("History"));
    }
}