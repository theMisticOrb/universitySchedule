package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.AcademicDepartmentDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.sdjpa.AcademicDepartmentRepository;

@Service
@Transactional
public class AcademicDepartmentService {

    private AcademicDepartmentRepository departmentRapository;
    private FacultyService facultyService;

    public AcademicDepartmentService(AcademicDepartmentRepository departmentRapository, FacultyService facultyService) {
        this.departmentRapository = departmentRapository;
        this.facultyService = facultyService;
    }

    public AcademicDepartment addOne(AcademicDepartmentDto department) {
        Faculty faculty = facultyService.findById(department.getFacultyId());

        AcademicDepartment newDepartment = new AcademicDepartment();
        newDepartment.setFaculty(faculty);
        newDepartment.setTitle(department.getTitle());
        return departmentRapository.save(newDepartment);

    }

    public Optional<AcademicDepartment> findByTitle(String title) {
        return departmentRapository.findByTitle(title);
    }

    public AcademicDepartment findById(Integer id) {
        return departmentRapository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found: " + id));
    }

    public List<AcademicDepartment> findAll() {
        return departmentRapository.findAll();
    }

    public AcademicDepartment update(AcademicDepartmentDto updated, Integer id) {
        AcademicDepartment department = findById(id);
        SpecializationMapper mapper = new SpecializationMapper();
        Faculty faculty = facultyService.findById(updated.getFacultyId());
        UpdateUtil.setIfNotNull(faculty, department::setFaculty);
        UpdateUtil.updateListIfNotNull(updated.getSpecializations(), department.getSpecializations(), mapper::toEntity);
        UpdateUtil.setIfNotNull(updated.getTitle(), department::setTitle);
        return department;
    }

    public void deleteById(Integer id) {
        departmentRapository.deleteById(id);
    }
    
    
}
