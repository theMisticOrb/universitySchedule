package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.helpers.UpdateUtils;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;

@Service
@Transactional
public class AcademicDepartmentService {

    private AcademicDepartmentRepository departmentRapository;

    public AcademicDepartmentService(AcademicDepartmentRepository departmentRapository) {
        this.departmentRapository = departmentRapository;
    }

    public AcademicDepartment addOne(AcademicDepartment department) {
            return departmentRapository.save(department);

    }

    public AcademicDepartment findById(Integer id) {
        return departmentRapository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found: " + id));
    }

    public List<AcademicDepartment> findAll() {
        return departmentRapository.findAll();
    }

    public AcademicDepartment update(AcademicDepartment updated) {
        AcademicDepartment department = findById(updated.getId());
        UpdateUtils.setIfNotNull(updated.getFaculty(), department::setFaculty);
        UpdateUtils.setIfNotNull(updated.getSpecializations(), department::setSpecializations);
        UpdateUtils.setIfNotNull(updated.getTitle(), department::setTitle);
        return department;
    }

    public void deleteById(Integer id) {
        departmentRapository.deleteById(id);
    }
}
