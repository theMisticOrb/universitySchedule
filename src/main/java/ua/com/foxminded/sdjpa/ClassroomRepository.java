package ua.com.foxminded.sdjpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Classroom;

@Repository
@Transactional
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    
    Optional<Classroom> findByName(String title);
}
