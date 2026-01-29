package ua.com.foxminded.sdjpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Faculty;

@Repository
@Transactional
public interface FacultyRepository extends JpaRepository<Faculty, Integer>{
    
    Optional<Faculty> findByTitle(String title);
    boolean existsByTitle(String title);

}
