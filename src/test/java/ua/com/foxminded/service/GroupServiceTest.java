package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.suppport.dto.GroupDtoTestBuilder;
import ua.com.foxminded.service.suppport.dto.StudentDtoTestBuilder;

class GroupServiceTest extends AbstractFullCrudServiceTest<Group, GroupCreateUpdateDto, Integer> {

    @Autowired
    private GroupService service;

    @Override
    protected FullCrudService<Group, GroupCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected GroupCreateUpdateDto createValidDto() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Specialization specialization = testDataFactory.persistSpecialization(department);

        return GroupDtoTestBuilder.aGroup().withSpecializationCode(specialization.getCode()).build();
    }

    @Override
    protected Integer getId(Group entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    @Test
    void addOne_shouldGenerateNameAndSetFirstYear_whenCalled() {
        Group saved = getService().addOne(createValidDto());

        assertNotNull(saved.getName());
        assertEquals(1, saved.getYearOfUniv().getCurentYearInUniv());
    }

    @Test
    void findByName_shouldReturnGroup_whenExists() {
        Group saved = getService().addOne(createValidDto());

        Group result = service.findByName(saved.getName());

        assertNotNull(result);
        assertEquals(saved.getId(), result.getId());
        assertEquals(saved.getName(), result.getName());
    }

    @Test
    void findByName_shouldThrowException_whenNotFound() {
        String nonexistentName = "NONEXISTENT.NAME.1.1";

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.findByName(nonexistentName));

        assertEquals("Group not found: " + nonexistentName, ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Group not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void update_shouldChangeSpecialization_whenSpecializationCodeProvided() {
        Group saved = getService().addOne(createValidDto());

        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Specialization newSpecialization = testDataFactory.persistSpecialization("Math", department);

        GroupCreateUpdateDto updateDto = GroupDtoTestBuilder.aGroup()
                .withSpecializationCode(newSpecialization.getCode()).build();

        Group result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals(newSpecialization.getId(), result.getSpecialization().getId());
        assertEquals("Math", result.getSpecialization().getTitle());
    }

    @Test
    void update_shouldNotOverwriteSpecialization_whenSpecializationCodeIsNull() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Specialization specialization = testDataFactory.persistSpecialization("Security", department);

        GroupCreateUpdateDto dto = GroupDtoTestBuilder.aGroup().withSpecializationCode(specialization.getCode())
                .build();
        Group saved = getService().addOne(dto);

        GroupCreateUpdateDto updateDto = new GroupCreateUpdateDto();
        updateDto.setSpecializationCode(null);

        Group result = service.fullUpdate(updateDto, saved.getId());

        assertEquals("Security", result.getSpecialization().getTitle());
    }

    @Test
    void fullUpdate_shouldFollowReplaceListSemantics_forStudents() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Specialization spec = testDataFactory.persistSpecialization(department);
        Group group = service.addOne(GroupDtoTestBuilder.aGroup().withSpecializationCode(spec.getCode()).build());

        GroupCreateUpdateDto twoStudents = new GroupCreateUpdateDto();
        twoStudents.setStudents(buildStudentDtos(2, group.getName()));
        service.fullUpdate(twoStudents, group.getId());

        assertEquals(2, service.findById(group.getId()).getStudents().size());

        GroupCreateUpdateDto threeStudents = new GroupCreateUpdateDto();
        threeStudents.setStudents(buildStudentDtos(3, group.getName()));
        service.fullUpdate(threeStudents, group.getId());

        assertEquals(3, service.findById(group.getId()).getStudents().size());

        GroupCreateUpdateDto nullStudents = new GroupCreateUpdateDto();
        nullStudents.setStudents(null);
        service.fullUpdate(nullStudents, group.getId());

        assertEquals(3, service.findById(group.getId()).getStudents().size());
    }

    private List<StudentCreateUpdateDto> buildStudentDtos(int count, String groupName) {
        return IntStream.range(0, count)
                .mapToObj(i -> StudentDtoTestBuilder.aStudent().withGroupName(groupName)
                        .withEmail("student")
                        .withFirstName("FirstName")
                        .withMiddleName("MiddleName")
                        .withLastName("LastName")
                        .withRoles(Collections.singleton(UserRole.STUDENT)).build())
                .collect(Collectors.toList());
    }

    @Test
    void addAll_shouldSaveAllGroupsWithIncrementingNumbers_whenCalled() {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Specialization specialization = testDataFactory.persistSpecialization(department);

        GroupCreateUpdateDto first = GroupDtoTestBuilder.aGroup().withSpecializationCode(specialization.getCode())
                .build();
        GroupCreateUpdateDto second = GroupDtoTestBuilder.aGroup().withSpecializationCode(specialization.getCode())
                .build();

        List<Group> result = service.addAll(Arrays.asList(first, second));

        assertEquals(2, result.size());
        assertNotNull(result.get(0).getId());
        assertNotNull(result.get(1).getId());
        assertEquals(false, result.get(0).getName().equals(result.get(1).getName()));
    }
}