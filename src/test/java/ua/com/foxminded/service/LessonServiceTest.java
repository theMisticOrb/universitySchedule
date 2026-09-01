package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.service.suppport.dto.LessonDtoTestBuilder;

class LessonServiceTest extends AbstractReadUpdateDeleteServiceTest<Lesson, LessonCreateUpdateDto, Integer> {

    @Autowired
    private LessonService service;

    private AcademicDepartment department;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        Faculty faculty = testDataFactory.persistFaculty();
        department = testDataFactory.persistDepartment(faculty);
        teacher = testDataFactory.persistTeacher(department);
    }

    @Override
    protected ReadUpdateDeleteService<Lesson, LessonCreateUpdateDto, Integer> getService() {
        return service;
    }

    @Override
    protected Lesson persistValidEntity() {
        LessonCreateUpdateDto dto = buildValidDto();
        return service.addOne(dto, teacher.getId());
    }

    @Override
    protected Integer getId(Lesson entity) {
        return entity.getId();
    }

    @Override
    protected Integer nonExistentId() {
        return 999;
    }

    private LessonCreateUpdateDto buildValidDto() {
        Classroom classroom = testDataFactory.persistClassroom();
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(teacher));
        Specialization specialization = testDataFactory.persistSpecialization(department);
        Group group = testDataFactory.persistGroup(specialization);

        return LessonDtoTestBuilder.aLesson().withClassroomName(classroom.getName())
                .withSubjectTitle(subject.getTitle()).withGroupName(group.getName()).build();
    }

    @Test
    void addOne_shouldSaveNewEntity_whenCalled() {
        assertNotNull(persistValidEntity().getId());
    }

    @Test
    void addOne_shouldThrowException_whenGroupAlreadyHasLessonAtThatTime() {
        Classroom classroomA = testDataFactory.persistClassroom();
        Classroom classroomB = testDataFactory.persistClassroom();
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(teacher));
        Specialization specialization = testDataFactory.persistSpecialization(department);
        Group group = testDataFactory.persistGroup(specialization);

        LessonCreateUpdateDto first = LessonDtoTestBuilder.aLesson().withClassroomName(classroomA.getName())
                .withSubjectTitle(subject.getTitle()).withGroupName(group.getName()).withWeekDay(WeekDay.MONDAY)
                .withClassHour(ClassHour.FIRST_CLASS).build();
        service.addOne(first, teacher.getId());

        Teacher anotherTeacher = testDataFactory.persistTeacher(department);
        LessonCreateUpdateDto conflicting = LessonDtoTestBuilder.aLesson().withClassroomName(classroomB.getName())
                .withSubjectTitle(subject.getTitle()).withGroupName(group.getName()).withWeekDay(WeekDay.MONDAY)
                .withClassHour(ClassHour.FIRST_CLASS).build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addOne(conflicting, anotherTeacher.getId()));

        assertEquals("The group already has a class at that time", ex.getMessage());
    }

    @Test
    void addOne_shouldThrowException_whenTeacherAlreadyHasLessonAtThatTime() {
        Classroom classroomA = testDataFactory.persistClassroom();
        Classroom classroomB = testDataFactory.persistClassroom();
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(teacher));
        Specialization specializationA = testDataFactory.persistSpecialization(department);
        Specialization specializationB = testDataFactory.persistSpecialization(department);
        Group groupA = testDataFactory.persistGroup(specializationA);
        Group groupB = testDataFactory.persistGroup(specializationB);

        LessonCreateUpdateDto first = LessonDtoTestBuilder.aLesson().withClassroomName(classroomA.getName())
                .withSubjectTitle(subject.getTitle()).withGroupName(groupA.getName()).withWeekDay(WeekDay.TUESDAY)
                .withClassHour(ClassHour.FIRST_CLASS).build();
        service.addOne(first, teacher.getId());

        LessonCreateUpdateDto conflicting = LessonDtoTestBuilder.aLesson().withClassroomName(classroomB.getName())

                .withSubjectTitle(subject.getTitle()).withGroupName(groupB.getName()).withWeekDay(WeekDay.TUESDAY)
                .withClassHour(ClassHour.FIRST_CLASS).build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addOne(conflicting, teacher.getId()));

        assertEquals("You already have a class at that time", ex.getMessage());
    }

    @Test
    void addOne_shouldThrowException_whenClassroomAlreadyOccupiedAtThatTime() {
        Classroom classroom = testDataFactory.persistClassroom();
        Subject subject = testDataFactory.persistSubject(department, Collections.singletonList(teacher));
        Specialization specializationA = testDataFactory.persistSpecialization(department);
        Specialization specializationB = testDataFactory.persistSpecialization(department);
        Group groupA = testDataFactory.persistGroup(specializationA);
        Group groupB = testDataFactory.persistGroup(specializationB);

        LessonCreateUpdateDto first = LessonDtoTestBuilder.aLesson().withClassroomName(classroom.getName())
                .withSubjectTitle(subject.getTitle()).withGroupName(groupA.getName()).withWeekDay(WeekDay.WEDNESDAY)
                .withClassHour(ClassHour.FIRST_CLASS).build();
        service.addOne(first, teacher.getId());

        Teacher anotherTeacher = testDataFactory.persistTeacher(department);
        LessonCreateUpdateDto conflicting = LessonDtoTestBuilder.aLesson().withClassroomName(classroom.getName())
                .withSubjectTitle(subject.getTitle()).withGroupName(groupB.getName()).withWeekDay(WeekDay.WEDNESDAY)
                .withClassHour(ClassHour.FIRST_CLASS).build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addOne(conflicting, anotherTeacher.getId()));

        assertEquals("The classroom is already occupied at that time", ex.getMessage());
    }

    @Test
    void findById_shouldThrowException_withCorrectMessage() {
        Integer nonexistentId = nonExistentId();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.findById(nonexistentId));

        assertEquals("Lesson not found: " + nonexistentId, ex.getMessage());
    }

    @Test
    void findBySubjectTitleAndWeekDay_shouldReturnMatchingLessons() {
        LessonCreateUpdateDto dto = buildValidDto();
        Lesson saved = service.addOne(dto, teacher.getId());

        List<Lesson> result = service.findBySubjectTitleAndWeekDay(dto.getSubjectTitle(), dto.getWeekDay());

        assertNotNull(result);
        assertTrue(result.stream().anyMatch(lesson -> lesson.getId().equals(saved.getId())));
    }

    @Test
    void update_shouldChangeSubject_whenSubjectTitleProvided() {
        Lesson saved = persistValidEntity();
        Subject newSubject = testDataFactory.persistSubject(department, Collections.singletonList(teacher));
        LessonCreateUpdateDto updateDto = new LessonCreateUpdateDto();
        updateDto.setSubjectTitle(newSubject.getTitle());

        Lesson result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals(newSubject.getId(), result.getSubject().getId());
    }

    @Test
    void update_shouldNotOverwriteFields_whenFieldIsNull() {
        Lesson saved = persistValidEntity();
        String originalSubjectTitle = saved.getSubject().getTitle();

        LessonCreateUpdateDto updateDto = new LessonCreateUpdateDto();
        updateDto.setSubjectTitle(null);

        Lesson result = service.fullUpdate(updateDto, saved.getId());

        assertEquals(originalSubjectTitle, result.getSubject().getTitle());
    }

    @Test
    void getWeeklyScheduleForTeacher_shouldGroupLessonsByWeekDay() {
        LessonCreateUpdateDto dto = buildValidDto();
        service.addOne(dto, teacher.getId());

        Map<WeekDay, List<Lesson>> schedule = service.getWeeklyScheduleForTeacher(teacher.getId());

        assertNotNull(schedule);
        assertEquals(WeekDay.values().length, schedule.size());
        assertTrue(schedule.get(dto.getWeekDay()).size() >= 1);
    }

    @Test
    void getWeeklyScheduleForGroup_shouldGroupLessonsByWeekDay() {
        LessonCreateUpdateDto dto = buildValidDto();
        service.addOne(dto, teacher.getId());

        Map<WeekDay, List<Lesson>> schedule = service.getWeeklyScheduleForGroup(dto.getGroupName());

        assertNotNull(schedule);
        assertTrue(schedule.get(dto.getWeekDay()).size() >= 1);
    }

    @Test
    void findOccupiedClassroomIds_shouldReturnClassroomIdsForGivenSlot() {
        LessonCreateUpdateDto dto = buildValidDto();
        Lesson saved = service.addOne(dto, teacher.getId());

        Set<Integer> occupiedIds = service.findOccupiedClassroomIds(dto.getWeekDay(), dto.getClassHour());

        assertTrue(occupiedIds.contains(saved.getClassroom().getId()));
    }
}