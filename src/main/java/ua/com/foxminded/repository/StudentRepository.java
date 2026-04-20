package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Student;

@Repository
@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {
    
    Optional<Student> findByEmail(String email);

    Optional<Student> findByLastNameAndFirstNameAndMiddleName(
            String lastName, 
            String firstName, 
            String middleName);

    boolean existsByLastNameAndFirstNameAndMiddleNameAndGroup_Id(
            String lastName, 
            String firstName, 
            String middleName,
            Integer groupId);
}
