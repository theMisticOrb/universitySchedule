package ua.com.foxminded.sdjpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;

@Repository
@Transactional
public interface AcademicDepartmentRepository extends JpaRepository<AcademicDepartment, Integer> {

    Optional<AcademicDepartment> findByTitle(String title);
    boolean existsByFacultyAndTitle(Faculty faculty, String title);

}
