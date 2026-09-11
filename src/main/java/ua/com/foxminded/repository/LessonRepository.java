package ua.com.foxminded.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    List<Lesson> findBySubject_IdAndWeekDay(Integer subjectId, WeekDay weekDay);

    List<Lesson> findByTeacher_IdAndWeekDay(Integer teacherId, WeekDay weekDay);

    List<Lesson> findByTeacher_Id(Integer teacherId);

    List<Lesson> findByGroup_Id(Integer groupId);

    List<Lesson> findByWeekDayAndClassHour(WeekDay weekDay, ClassHour classHour);

    boolean existsBySubject_IdAndClassroom_Name(Integer subjectId, String classroomName);

    boolean existsByClassroom_NameAndWeekDayAndClassHour(String classroomName, WeekDay weekDay, ClassHour classHour);

    boolean existsByGroup_IdAndWeekDayAndClassHour(Integer groupId, WeekDay weekDay, ClassHour classHour);

    boolean existsByTeacher_IdAndWeekDayAndClassHour(Integer teacherId, WeekDay weekDay, ClassHour classHour);

}
