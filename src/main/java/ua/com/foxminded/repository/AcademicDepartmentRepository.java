package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.AcademicDepartment;

@Repository
@Transactional
public interface AcademicDepartmentRepository extends JpaRepository<AcademicDepartment, Integer> {

    Optional<AcademicDepartment> findByTitle(String title);
    boolean existsByFaculty_IdAndTitle(Integer facultyId, String title);

}
