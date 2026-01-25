package ua.com.foxminded.sdjpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Teacher;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Integer>{
    
    Optional<Teacher> findByLastNameAndFirstNameAndMiddleName(
            String lastName, 
            String firstName, 
            String middleName);
}
