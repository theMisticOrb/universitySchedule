package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.repository.AcademicDepartmentRepository;

@Service
@Transactional
public class AcademicDepartmentService {

    private AcademicDepartmentRepository departmentRepository;
    private FacultyService facultyService;

    public AcademicDepartmentService(AcademicDepartmentRepository departmentRapository, FacultyService facultyService) {
        this.departmentRepository = departmentRapository;
        this.facultyService = facultyService;
    }

    public AcademicDepartment addOne(AcademicDepartmentCreateUpdateDto dto) {
        Faculty faculty = facultyService.findById(dto.getFacultyId());

        if (departmentRepository.existsByFaculty_IdAndTitle(faculty.getId(), dto.getTitle())) {
            throw new DuplicateEntityException(
                    "Department with title: " + dto.getTitle() + " alredy exists in this faculty!");
        }

        AcademicDepartment newDepartment = new AcademicDepartment();
        newDepartment.setFaculty(faculty);
        newDepartment.setTitle(dto.getTitle());
        return departmentRepository.save(newDepartment);

    }

    public AcademicDepartment findByTitle(String title) {
        return departmentRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found: " + title));
    }

    public AcademicDepartment findById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found: " + id));
    }

    public List<AcademicDepartment> findAll() {
        return departmentRepository.findAll();
    }

    public AcademicDepartment update(AcademicDepartmentCreateUpdateDto updated, Integer id) {

        AcademicDepartment department = findById(id);
        SpecializationMapper mapper = new SpecializationMapper();

        if (updated.getFacultyId() != null) {
            Faculty faculty = facultyService.findById(updated.getFacultyId());
            UpdateUtil.setIfNotNull(faculty, department::setFaculty);
        }
        UpdateUtil.updateListIfNotNull(updated.getSpecializations(), department.getSpecializations(), mapper::toEntity);
        UpdateUtil.setIfNotNull(updated.getTitle(), department::setTitle);
        return department;
    }

    public void deleteById(Integer id) {
        departmentRepository.deleteById(id);
    }

}
