package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Group;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String name);

    boolean existsByFaculty_IdAndDepartment_IdAndSpecialization_IdAndName(
            Integer facultyId, 
            Integer departmentId,
            Integer specializationId, 
            String name);

    int countBySpecialization_Id(Integer specializationId);
}
