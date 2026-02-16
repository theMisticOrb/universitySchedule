package ua.com.foxminded.sdjpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Teacher;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Integer>{

}
