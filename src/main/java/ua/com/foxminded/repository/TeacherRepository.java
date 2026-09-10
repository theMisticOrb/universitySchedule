package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Teacher;

@Repository
@Transactional
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByLastNameAndFirstNameAndMiddleName(String lastName, String firstName, String middleName);

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findById(Integer teacherId);

    boolean existsByLastNameAndFirstNameAndMiddleNameAndDepartment_Id(String lastName, String firstName,
            String middleName, Integer departmentId);

    boolean existsByIdAndSubjects_Title(Integer teacherId, String subjectTitle);
}
