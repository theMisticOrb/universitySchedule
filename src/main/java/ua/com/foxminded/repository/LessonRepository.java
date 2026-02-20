package ua.com.foxminded.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.enums.WeekDay;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Integer>{

    List<Lesson> findBySubject_TitleAndWeekDay(String title, WeekDay weekDay);
    boolean existsBySubject_TitleAndClassroom_Name(String subjectTitle, String classroomName);
}
