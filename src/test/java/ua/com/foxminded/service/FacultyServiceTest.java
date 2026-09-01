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
import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.service.suppport.TestDataFactory;
import ua.com.foxminded.service.suppport.dto.AcademicDepartmentDtoTestBuilder;
import ua.com.foxminded.service.suppport.dto.FacultyDtoTestBuilder;

class FacultyServiceTest extends AbstractFullCrudServiceTest<Faculty, FacultyCreateUpdateDto, Integer> {

    @Autowired
    private FacultyService service;

    @Override
    protected FullCrudService<Faculty, FacultyCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected FacultyCreateUpdateDto createValidDto() {
        return FacultyDtoTestBuilder.aFaculty().build();
    }

    @Override
    protected Integer getId(Faculty entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    @Test
    void findByTitle_shouldReturnFaculty_whenExists() {
        FacultyCreateUpdateDto dto = FacultyDtoTestBuilder.aFaculty().build();
        service.addOne(dto);

        Faculty result = service.findByTitle(dto.getTitle());

        assertNotNull(result);
        assertEquals(dto.getTitle(), result.getTitle());
    }

    @Test
    void findByTitle_shouldThrowException_whenNotFound() {
        String nonexistentTitle = "Nonexistent Faculty";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByTitle(nonexistentTitle));

        assertEquals("Faculty not found: " + nonexistentTitle, ex.getMessage());
    }

    @Test
    void findByCode_shouldReturnFaculty_whenExists() {
        FacultyCreateUpdateDto dto = FacultyDtoTestBuilder.aFaculty().build();
        service.addOne(dto);

        Faculty result = service.findByCode(dto.getCode());

        assertNotNull(result);
        assertEquals(dto.getCode(), result.getCode());
    }

    @Test
    void findByCode_shouldThrowException_whenNotFound() {
        String nonexistentCode = "NON_EXISTENT_CODE";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByCode(nonexistentCode));

        assertEquals("Faculty with this code are not found: " + nonexistentCode, ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Faculty not found by id: " + nonexistentId, ex.getMessage());
    }

    @Test
    void addOne_shouldThrowDuplicateEntityException_whenTitleAlreadyExists() {
        FacultyCreateUpdateDto dto = FacultyDtoTestBuilder.aFaculty().withTitle("Engineering").build();
        service.addOne(dto);

        FacultyCreateUpdateDto duplicateDto = FacultyDtoTestBuilder.aFaculty().withTitle("Engineering").build();

        DuplicateEntityException ex = assertThrows(DuplicateEntityException.class, () -> service.addOne(duplicateDto));

        assertEquals("Faculty with title: Engineering alredy exists!", ex.getMessage());
    }

    @Test
    void update_shouldChangeTitleAndCode_whenProvided() {
        FacultyCreateUpdateDto dto = FacultyDtoTestBuilder.aFaculty().withTitle("Engineering").build();
        Faculty saved = service.addOne(dto);

        FacultyCreateUpdateDto updateDto = FacultyDtoTestBuilder.aFaculty().withTitle("Economy").build();

        Faculty result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals("Economy", result.getTitle());
    }

    @Test
    void update_shouldNotOverwriteNullFields_whenFieldIsNull() {
        FacultyCreateUpdateDto dto = FacultyDtoTestBuilder.aFaculty().withTitle("Engineering").build();
        Faculty saved = service.addOne(dto);

        FacultyCreateUpdateDto updateDto = new FacultyCreateUpdateDto();
        updateDto.setTitle(null);

        Faculty result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("Engineering", result.getTitle());
    }

    @Test
    void fullUpdate_shouldFollowReplaceListSemantics_forFaculties() {
        Faculty faculty = testDataFactory.persistFaculty();

        FacultyCreateUpdateDto twoDepartments = new FacultyCreateUpdateDto();
        twoDepartments.setDepartments(buildDepartmentDtos(2));
        service.fullUpdate(twoDepartments, faculty.getId());

        assertEquals(2, service.findById(faculty.getId()).getDepartments().size());

        FacultyCreateUpdateDto threeSpecs = new FacultyCreateUpdateDto();
        threeSpecs.setDepartments(buildDepartmentDtos(3));
        service.fullUpdate(threeSpecs, faculty.getId());

        assertEquals(3, service.findById(faculty.getId()).getDepartments().size());

        FacultyCreateUpdateDto nullSpecs = new FacultyCreateUpdateDto();
        nullSpecs.setDepartments(null);
        nullSpecs.setTitle("Unrelated title change");
        service.fullUpdate(nullSpecs, faculty.getId());

        assertEquals(3, service.findById(faculty.getId()).getDepartments().size());
    }
    
    private List<AcademicDepartmentCreateUpdateDto> buildDepartmentDtos(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> AcademicDepartmentDtoTestBuilder.anAcademicDepartment()
                        .withTitle(TestDataFactory.uniqueName("Department")).withFacultyCode("UNUSED").build())
                .collect(Collectors.toList());
    }
    
    @Test
    void count_shouldReturnNumberOfFaculties() {
        service.addOne(FacultyDtoTestBuilder.aFaculty().build());
        service.addOne(FacultyDtoTestBuilder.aFaculty().build());

        assertEquals(2, service.count());
    }

    @Test
    void count_shouldReturnZero_whenNoFacultiesExist() {
        assertEquals(0, service.count());
    }

    @Test
    void createFaculties_shouldSaveAllProvidedFaculties() {
        FacultyCreateUpdateDto first = FacultyDtoTestBuilder.aFaculty().withTitle("Engineering").build();
        FacultyCreateUpdateDto second = FacultyDtoTestBuilder.aFaculty().withTitle("Economy").build();

        service.createFaculties(Arrays.asList(first, second));

        List<Faculty> allFaculties = service.findAll();
        List<String> titles = allFaculties.stream().map(Faculty::getTitle).collect(Collectors.toList());

        assertTrue(titles.contains("Engineering"));
        assertTrue(titles.contains("Economy"));
    }
}