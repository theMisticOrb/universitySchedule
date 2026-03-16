package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.FacultyRepository;
import ua.com.foxminded.repository.SpecializationRepository;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
class GroupServiceTest {

    @Autowired
    private GroupService service;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private AcademicDepartmentRepository departmentRepository;
    @Autowired
    private SpecializationRepository specializationRepository;

    private GroupCreateUpdateDto group = new GroupCreateUpdateDto();

    @BeforeEach
    void setUp() {
        Faculty faculty = new Faculty();
        faculty.setCode("EN");
        faculty.setTitle("Engineering");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Computer Science");
        computerScience.setCode("CS");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        Specialization specialization = new Specialization();
        specialization.setDepartment(csDepartment);
        specialization.setTitle("Security");
        specialization.setCode("SE");
        Specialization security = specializationRepository.save(specialization);

        group.setSpecializationCode(security.getCode());

    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {

        Group firstGroup = service.addOne(group);

        assertNotNull(firstGroup.getId());
        assertEquals(1, firstGroup.getYearOfUniv().getCurentYearInUniv());
    }

    @Test
    void findById_shouldFindEntityById_whenCalledIfEntityExists() {

        Group saved = service.addOne(group);
        Group found = service.findById(saved.getId());

        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    void findById_shouldThrowException_whenCalledifEntityDoesNotExist() {
        int nonexistentId = 34;
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));
        assertEquals("Group not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findAll_shouldGetListOfAllEntities_whenCalledIfNotEmpty() {

        List<Group> allGroups = service.findAll();

        assertNotNull(allGroups);

    }

    @Test
    void findByTitle_shouldReturnGroup_whenExists() {
        Group saved = service.addOne(group);
        String groupName = saved.getName();

        Group result = service.findByName(groupName);

        assertNotNull(result);
        assertEquals(groupName, result.getName());
        assertEquals(saved.getId(), result.getId());
    }

    @Test
    void update_shouldUpdateEntity_whenCalled() {
        Group firstGroup = service.addOne(group);

        Faculty faculty = new Faculty();
        faculty.setTitle("Data Science");
        faculty.setCode("DS");
        Faculty engineeringFaculty = facultyRepository.save(faculty);

        AcademicDepartment computerScience = new AcademicDepartment();
        computerScience.setTitle("Analitics");
        computerScience.setCode("AN");
        computerScience.setFaculty(engineeringFaculty);
        AcademicDepartment csDepartment = departmentRepository.save(computerScience);

        Specialization specialization = new Specialization();
        specialization.setTitle("Math");
        specialization.setCode("MT");
        specialization.setDepartment(csDepartment);
        Specialization mathSpecialization = specializationRepository.save(specialization);

        GroupCreateUpdateDto firstGroupUpdated = new GroupCreateUpdateDto();
        firstGroupUpdated.setSpecializationCode(mathSpecialization.getCode());
        Group result = service.update(firstGroupUpdated, firstGroup.getId());

        assertEquals(firstGroup.getId(), result.getId());
        assertEquals(mathSpecialization.getId(), result.getSpecialization().getId());
        assertEquals(mathSpecialization.getTitle(), result.getSpecialization().getTitle());

    }

    @Test
    void update_shouldNotOwerwriteNullFields_whenOneOfFieldsIsNull() {

        Group firstGroup = service.addOne(group);

        GroupCreateUpdateDto computerScienceUpdated = new GroupCreateUpdateDto();
        computerScienceUpdated.setSpecializationCode(null);
        Group result = service.update(computerScienceUpdated, firstGroup.getId());

        assertEquals("Security", result.getSpecialization().getTitle());
    }

    @Test
    void delete_shouldDeleteEntityById_whenEntityExists() {
        Group firstGroup = service.addOne(group);
        int id = firstGroup.getId();

        assertNotNull(service.findById(id));
        service.deleteById(id);
        assertThrows(EntityNotFoundException.class, () -> service.findById(id));
    }

}
