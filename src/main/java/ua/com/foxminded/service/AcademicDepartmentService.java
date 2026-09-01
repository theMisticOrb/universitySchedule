package ua.com.foxminded.service;

import java.util.List;
import java.util.stream.Collectors;

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
import ua.com.foxminded.repository.SpecializationRepository;

@Service
@Transactional
public class AcademicDepartmentService
        implements FullCrudService<AcademicDepartment, AcademicDepartmentCreateUpdateDto, Integer> {

    private final AcademicDepartmentRepository departmentRepository;
    private final SpecializationRepository specializationRepo;
    private final FacultyService facultyService;
    private final SpecializationMapper specializationMapper;
    private final AcademicDepartmentMapper departmentMapper;

    public AcademicDepartmentService(AcademicDepartmentRepository departmentRepository,
            SpecializationRepository specializationRepo, FacultyService facultyService,
            SpecializationMapper specializationMapper, AcademicDepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.specializationRepo = specializationRepo;
        this.facultyService = facultyService;
        this.specializationMapper = specializationMapper;
        this.departmentMapper = departmentMapper;
    }

    @Override
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

    @Override
    public AcademicDepartment findById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Academic Department not found by id: " + id));
    }

    @Override
    public List<AcademicDepartment> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public AcademicDepartment fullUpdate(AcademicDepartmentCreateUpdateDto updated, Integer id) {

        AcademicDepartment department = findById(id);

        if (updated.getFacultyCode() != null) {
            Faculty faculty = facultyService.findByCode(updated.getFacultyCode());
            UpdateUtil.setIfNotNull(faculty, department::setFaculty);
        }
        UpdateUtil.replaceList(department.getSpecializations(), updated.getSpecializations(),
                updatedDto -> specializationMapper.toEntity(updatedDto, department), specializationRepo);
        UpdateUtil.setIfNotNull(updated.getTitle(), department::setTitle);
        UpdateUtil.setIfNotNull(updated.getCode(), department::setCode);
        return department;
    }

    @Override
    public void deleteById(Integer id) {
        departmentRepository.deleteById(id);
    }

    public void createDepartments(List<AcademicDepartmentCreateUpdateDto> list) {
        List<AcademicDepartment> departments = list.stream().map(this::addOne).collect(Collectors.toList());
        departmentRepository.saveAllAndFlush(departments);

    }

}
