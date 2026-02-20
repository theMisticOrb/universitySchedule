package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Specialization;

@Repository
@Transactional
public interface SpecializationRepository extends JpaRepository<Specialization, Integer>{

    Optional<Specialization> findByTitle(String title);
    boolean existsByTitleAndDepartment_Id(String title, Integer departmentId);
}
