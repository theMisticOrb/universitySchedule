package ua.com.foxminded.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Subject;

@Repository
@Transactional
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    Optional<Subject> findByTitle(String title);

    @Query("SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId")
    List<Subject> findByTeacherId(@Param("teacherId") Integer teacherId);

    boolean existsByTitle(String title);
}
