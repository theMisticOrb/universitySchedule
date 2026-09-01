package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.suppport.TestDataFactory;
import ua.com.foxminded.service.suppport.dto.AcademicDepartmentDtoTestBuilder;
import ua.com.foxminded.service.suppport.dto.SpecializationDtoTestBuilder;

class AcademicDepartmentServiceTest
        extends AbstractFullCrudServiceTest<AcademicDepartment, AcademicDepartmentCreateUpdateDto, Integer> {

    @Autowired
    private AcademicDepartmentService service;

    @Override
    protected FullCrudService<AcademicDepartment, AcademicDepartmentCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected AcademicDepartmentCreateUpdateDto createValidDto() {
        Faculty faculty = testDataFactory.persistFaculty();
        return AcademicDepartmentDtoTestBuilder.anAcademicDepartment().withFacultyCode(faculty.getCode()).build();
    }

    @Override
    protected Integer getId(AcademicDepartment entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    @Test
    void findByTitle_shouldReturnDepartment_whenExists() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartmentCreateUpdateDto dto = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withFacultyCode(faculty.getCode()).build();
        service.addOne(dto);

        AcademicDepartment result = service.findByTitle(dto.getTitle());

        assertNotNull(result);
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(faculty.getId(), result.getFaculty().getId());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Academic Department not found by id: " + nonexistentId, ex.getMessage());
    }

    @Test
    void fullUpdate_shouldChangeFaculty_whenFacultyCodeProvided() {
        Faculty engineering = testDataFactory.persistFaculty("Engineering");
        Faculty economy = testDataFactory.persistFaculty("Economy");

        AcademicDepartmentCreateUpdateDto dto = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withTitle("Computer Science").withFacultyCode(engineering.getCode()).build();
        AcademicDepartment department = service.addOne(dto);

        AcademicDepartmentCreateUpdateDto updateDto = new AcademicDepartmentCreateUpdateDto();
        updateDto.setFacultyCode(economy.getCode());

        AcademicDepartment result = service.fullUpdate(updateDto, department.getId());

        assertEquals(department.getId(), result.getId());
        assertEquals("Computer Science", result.getTitle());
        assertEquals(economy.getTitle(), result.getFaculty().getTitle());
    }

    @Test
    void fullUpdate_shouldNotOverwriteNullFields_whenFieldIsNull() {
        Faculty engineering = testDataFactory.persistFaculty("Engineering");

        AcademicDepartmentCreateUpdateDto dto = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withTitle("Computer Science").withFacultyCode(engineering.getCode()).build();
        AcademicDepartment department = service.addOne(dto);

        AcademicDepartmentCreateUpdateDto updateDto = new AcademicDepartmentCreateUpdateDto();
        updateDto.setTitle(null);
        updateDto.setFacultyCode(engineering.getCode());

        AcademicDepartment result = service.fullUpdate(updateDto, department.getId());

        assertEquals("Computer Science", result.getTitle());
        assertEquals(engineering.getTitle(), result.getFaculty().getTitle());
    }

    @Test
    void fullUpdate_shouldFollowReplaceListSemantics_forSpecializations() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = service.addOne(
                AcademicDepartmentDtoTestBuilder.anAcademicDepartment().withFacultyCode(faculty.getCode()).build());

        AcademicDepartmentCreateUpdateDto twoSpecs = new AcademicDepartmentCreateUpdateDto();
        twoSpecs.setSpecializations(buildSpecializationDtos(2));
        service.fullUpdate(twoSpecs, department.getId());

        assertEquals(2, service.findById(department.getId()).getSpecializations().size());

        AcademicDepartmentCreateUpdateDto threeSpecs = new AcademicDepartmentCreateUpdateDto();
        threeSpecs.setSpecializations(buildSpecializationDtos(3));
        service.fullUpdate(threeSpecs, department.getId());

        assertEquals(3, service.findById(department.getId()).getSpecializations().size());

        AcademicDepartmentCreateUpdateDto nullSpecs = new AcademicDepartmentCreateUpdateDto();
        nullSpecs.setSpecializations(null);
        nullSpecs.setTitle("Unrelated title change");
        service.fullUpdate(nullSpecs, department.getId());

        assertEquals(3, service.findById(department.getId()).getSpecializations().size());
    }

    @Test
    void findByCode_shouldReturnDepartment_whenExists() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartmentCreateUpdateDto dto = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withFacultyCode(faculty.getCode()).build();
        service.addOne(dto);

        AcademicDepartment result = service.findByCode(dto.getCode());

        assertNotNull(result);
        assertEquals(dto.getCode(), result.getCode());
    }

    @Test
    void findByCode_shouldThrowException_whenNotFound() {
        String nonexistentCode = "NON_EXISTENT_CODE";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByCode(nonexistentCode));

        assertEquals("Academic Department not found by code: " + nonexistentCode, ex.getMessage());
    }

    @Test
    void findByTitle_shouldThrowException_whenNotFound() {
        String nonexistentTitle = "Nonexistent Title";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByTitle(nonexistentTitle));

        assertEquals("Academic Department not found by title: " + nonexistentTitle, ex.getMessage());
    }

    @Test
    void addOne_shouldThrowDuplicateEntityException_whenTitleAlreadyExistsInFaculty() {
        Faculty faculty = testDataFactory.persistFaculty();

        AcademicDepartmentCreateUpdateDto dto = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withTitle("Computer Science").withCode("CS-1").withFacultyCode(faculty.getCode()).build();
        service.addOne(dto);

        AcademicDepartmentCreateUpdateDto duplicateDto = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withTitle("Computer Science").withCode("CS-2").withFacultyCode(faculty.getCode()).build();

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class, () -> service.addOne(duplicateDto));

        assertEquals("Department with title: Computer Science alredy exists in this faculty!", ex.getMessage());
    }

    @Test
    void createDepartments_shouldSaveAllProvidedDepartments() {
        Faculty faculty = testDataFactory.persistFaculty();

        AcademicDepartmentCreateUpdateDto first = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withTitle("Computer Science").withFacultyCode(faculty.getCode()).build();
        AcademicDepartmentCreateUpdateDto second = AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                .withTitle("Multimedia").withFacultyCode(faculty.getCode()).build();

        service.createDepartments(Arrays.asList(first, second));

        List<AcademicDepartment> allDepartments = service.findAll();
        List<String> titles = allDepartments.stream().map(AcademicDepartment::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("Computer Science"));
        assertTrue(titles.contains("Multimedia"));
    }

    private List<SpecializationCreateUpdateDto> buildSpecializationDtos(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> SpecializationDtoTestBuilder.aSpecialization()
                        .withTitle(TestDataFactory.uniqueName("Specialization")).withDepartmentCode("UNUSED").build())
                .collect(Collectors.toList());
    }

}
