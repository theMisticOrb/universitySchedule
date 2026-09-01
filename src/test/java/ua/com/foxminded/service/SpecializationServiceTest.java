package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.suppport.dto.SpecializationDtoTestBuilder;

class SpecializationServiceTest
        extends AbstractFullCrudServiceTest<Specialization, SpecializationCreateUpdateDto, Integer> {

    @Autowired
    private SpecializationService service;

    @Override
    protected FullCrudService<Specialization, SpecializationCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected SpecializationCreateUpdateDto createValidDto() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);

        return SpecializationDtoTestBuilder.aSpecialization().withDepartmentCode(department.getCode()).build();
    }

    @Override
    protected Integer getId(Specialization entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    @Test
    void findByTitle_shouldReturnSpecialization_whenExists() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        SpecializationCreateUpdateDto dto = SpecializationDtoTestBuilder.aSpecialization()
                .withDepartmentCode(department.getCode()).build();
        service.addOne(dto);

        Specialization result = service.findByTitle(dto.getTitle());

        assertNotNull(result);
        assertEquals(dto.getTitle(), result.getTitle());
    }

    @Test
    void findByTitle_shouldThrowException_whenNotFound() {
        String nonexistentTitle = "Nonexistent Specialization";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByTitle(nonexistentTitle));

        assertEquals("Specialization not found by title: " + nonexistentTitle, ex.getMessage());
    }

    @Test
    void findByCode_shouldReturnSpecialization_whenExists() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        SpecializationCreateUpdateDto dto = SpecializationDtoTestBuilder.aSpecialization()
                .withDepartmentCode(department.getCode()).build();
        service.addOne(dto);

        Specialization result = service.findByCode(dto.getCode());

        assertNotNull(result);
        assertEquals(dto.getCode(), result.getCode());
    }

    @Test
    void findByCode_shouldThrowException_whenNotFound() {
        String nonexistentCode = "NON_EXISTENT_CODE";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByCode(nonexistentCode));

        assertEquals("Specialization not found by code: " + nonexistentCode, ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Specialization not found by id: " + nonexistentId, ex.getMessage());
    }

    @Test
    void addOne_shouldThrowDuplicateEntityException_whenTitleAlreadyExistsInDepartment() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);

        SpecializationCreateUpdateDto dto = SpecializationDtoTestBuilder.aSpecialization().withTitle("Security")
                .withCode("SEC-1").withDepartmentCode(department.getCode()).build();
        service.addOne(dto);

        SpecializationCreateUpdateDto duplicateDto = SpecializationDtoTestBuilder.aSpecialization()
                .withTitle("Security").withCode("SEC-2").withDepartmentCode(department.getCode()).build();

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class, () -> service.addOne(duplicateDto));

        assertEquals("Specialization with title: Security alredy exists in this academic department!", ex.getMessage());
    }

    @Test
    void update_shouldChangeDepartment_whenDepartmentCodeProvided() {
        Faculty engineering = testDataFactory.persistFaculty("Engineering");
        AcademicDepartment computerScience = testDataFactory.persistDepartment("Computer Science", engineering);

        SpecializationCreateUpdateDto dto = SpecializationDtoTestBuilder.aSpecialization().withTitle("Security")
                .withDepartmentCode(computerScience.getCode()).build();
        Specialization saved = service.addOne(dto);

        Faculty math = testDataFactory.persistFaculty("Math");
        AcademicDepartment analysis = testDataFactory.persistDepartment("Mathematical analysis", math);

        SpecializationCreateUpdateDto updateDto = new SpecializationCreateUpdateDto();
        updateDto.setDepartmentCode(analysis.getCode());

        Specialization result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals(math.getId(), result.getDepartment().getFaculty().getId());
        assertEquals(analysis.getTitle(), result.getDepartment().getTitle());
    }

    @Test
    void update_shouldNotOverwriteDepartment_whenDepartmentCodeIsNull() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment("Computer Science", faculty);

        SpecializationCreateUpdateDto dto = SpecializationDtoTestBuilder.aSpecialization()
                .withDepartmentCode(department.getCode()).build();
        Specialization saved = service.addOne(dto);

        SpecializationCreateUpdateDto updateDto = new SpecializationCreateUpdateDto();
        updateDto.setDepartmentCode(null);

        Specialization result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("Computer Science", result.getDepartment().getTitle());
    }

    @Test
    void createSpecializations_shouldSaveAllProvidedSpecializations() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);

        SpecializationCreateUpdateDto first = SpecializationDtoTestBuilder.aSpecialization().withTitle("Security")
                .withDepartmentCode(department.getCode()).build();
        SpecializationCreateUpdateDto second = SpecializationDtoTestBuilder.aSpecialization().withTitle("Networks")
                .withDepartmentCode(department.getCode()).build();

        service.createSpecializations(Arrays.asList(first, second));

        List<Specialization> all = service.findAll();
        List<String> titles = all.stream().map(Specialization::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("Security"));
        assertTrue(titles.contains("Networks"));
    }
}