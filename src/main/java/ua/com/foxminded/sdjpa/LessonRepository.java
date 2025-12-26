package ua.com.foxminded.sdjpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Lesson;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Integer>{

}
