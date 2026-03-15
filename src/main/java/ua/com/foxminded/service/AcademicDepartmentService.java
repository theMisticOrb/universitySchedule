package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.repository.AcademicDepartmentRepository;

@Service
@Transactional
public class AcademicDepartmentService {

    private AcademicDepartmentRepository departmentRepository;
    private FacultyService facultyService;
    private SpecializationMapper specializationMapper;
    private AcademicDepartmentMapper departmentMapper;

    public AcademicDepartmentService(AcademicDepartmentRepository departmentRepository, FacultyService facultyService,
            SpecializationMapper specializationMapper, AcademicDepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.facultyService = facultyService;
        this.specializationMapper = specializationMapper;
        this.departmentMapper = departmentMapper;
    }

    public AcademicDepartment addOne(AcademicDepartmentCreateUpdateDto dto) {

        Faculty faculty = facultyService.findByCode(dto.getFacultyCode());
        AcademicDepartment newDepartment = departmentMapper.toEntity(dto, faculty);

        try {
            return departmentRepository.save(newDepartment);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(
                    "Department with title: " + dto.getTitle() + " alredy exists in this faculty!");
        }

    }

    public AcademicDepartment findByTitle(String title) {
        return departmentRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found by title: " + title));
    }

    public AcademicDepartment findByCode(String code) {
        return departmentRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found by code: " + code));
    }

    public AcademicDepartment findById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found by id: " + id));
    }

    public List<AcademicDepartment> findAll() {
        return departmentRepository.findAll();
    }

    public AcademicDepartment update(AcademicDepartmentCreateUpdateDto updated, Integer id) {

        AcademicDepartment department = findById(id);

        if (updated.getFacultyCode() != null) {
            Faculty faculty = facultyService.findByCode(updated.getFacultyCode());
            UpdateUtil.setIfNotNull(faculty, department::setFaculty);
        }
        UpdateUtil.updateListIfNotNull(updated.getSpecializations(),
                updatedDto -> specializationMapper.toEntity(updatedDto, department));
        UpdateUtil.setIfNotNull(updated.getTitle(), department::setTitle);
        UpdateUtil.setIfNotNull(updated.getCode(), department::setCode);
        return department;
    }

    public void deleteById(Integer id) {
        departmentRepository.deleteById(id);
    }

}
