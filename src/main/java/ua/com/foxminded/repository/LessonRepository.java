package ua.com.foxminded.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    String SCHEDULE_SELECT = "SELECT new ua.com.foxminded.dto.lesson.LessonResponseDto("
            + "l.id, l.classroom.name, l.subject.id, l.subject.title, l.classHour, l.weekDay, l.group.id, l.group.name) "
            + "FROM Lesson l ";

    @Query(SCHEDULE_SELECT + "WHERE l.subject.id = :subjectId AND l.weekDay = :weekDay")
    List<LessonResponseDto> findBySubject_IdAndWeekDay(@Param("subjectId") Integer subjectId,
            @Param("weekDay") WeekDay weekDay);

    @Query(SCHEDULE_SELECT + "WHERE l.teacher.id = :teacherId AND l.weekDay = :weekDay")
    List<LessonResponseDto> findByTeacher_IdAndWeekDay(@Param("teacherId") Integer teacherId,
            @Param("weekDay") WeekDay weekDay);

    @Query(SCHEDULE_SELECT + "WHERE l.teacher.id = :teacherId")
    List<LessonResponseDto> findByTeacher_Id(@Param("teacherId") Integer teacherId);

    @Query(SCHEDULE_SELECT + "WHERE l.group.id = :groupId")
    List<LessonResponseDto> findByGroup_Id(@Param("groupId") Integer groupId);

    @Query(SCHEDULE_SELECT + "WHERE l.weekDay = :weekDay AND l.classHour = :classHour")
    List<LessonResponseDto> findByWeekDayAndClassHour(@Param("weekDay") WeekDay weekDay,
            @Param("classHour") ClassHour classHour);

    @Query("SELECT l.classroom.id FROM Lesson l WHERE l.weekDay = :weekDay AND l.classHour = :classHour")
    Set<Integer> findOccupiedClassroomIds(@Param("weekDay") WeekDay weekDay, @Param("classHour") ClassHour classHour);

    boolean existsBySubject_IdAndClassroom_Name(Integer subjectId, String classroomName);

    boolean existsByClassroom_NameAndWeekDayAndClassHour(String classroomName, WeekDay weekDay, ClassHour classHour);

    boolean existsByGroup_IdAndWeekDayAndClassHour(Integer groupId, WeekDay weekDay, ClassHour classHour);

    boolean existsByTeacher_IdAndWeekDayAndClassHour(Integer teacherId, WeekDay weekDay, ClassHour classHour);

}
