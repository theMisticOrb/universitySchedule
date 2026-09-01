package ua.com.foxminded.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.mapper.FacultyMapper;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.FacultyRepository;

@Service
@Transactional
public class FacultyService implements FullCrudService<Faculty, FacultyCreateUpdateDto, Integer> {

    private final FacultyRepository facultyRepository;
    private final AcademicDepartmentRepository departmentRepository;
    private final AcademicDepartmentMapper departmentMapper;
    private final FacultyMapper facultyMapper;

    public FacultyService(FacultyRepository facultyRepository, AcademicDepartmentRepository departmentRepository,
            AcademicDepartmentMapper departmentMapper, FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.facultyMapper = facultyMapper;
    }

    @Override
    public Faculty addOne(FacultyCreateUpdateDto dto) {

        Faculty faculty = facultyMapper.toEntity(dto);

        try {
            return facultyRepository.save(faculty);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Faculty with title: " + dto.getTitle() + " alredy exists!");

        }
    }

    @Override
    public Faculty findById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found by id: " + id));
    }

    public Faculty findByCode(String code) {
        return facultyRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Faculty with this code are not found: " + code));
    }

    public Faculty findByTitle(String title) {
        return facultyRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found: " + title));
    }

    @Override
    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    @Override
    public Faculty fullUpdate(FacultyCreateUpdateDto updated, Integer id) {
        Faculty faculty = findById(id);
        
        UpdateUtil.replaceList(faculty.getDepartments(), updated.getDepartments(),
                updatedDto -> departmentMapper.toEntity(updatedDto, faculty), departmentRepository);

        UpdateUtil.setIfNotNull(updated.getTitle(), faculty::setTitle);
        UpdateUtil.setIfNotNull(updated.getCode(), faculty::setCode);
        return faculty;
    }

    @Override
    public void deleteById(Integer id) {
        facultyRepository.deleteById(id);
    }

    public long count() {
        return facultyRepository.count();
    }

    public void createFaculties(List<FacultyCreateUpdateDto> list) {
        List<Faculty> faculties = list.stream().map(this::addOne).collect(Collectors.toList());
        facultyRepository.saveAllAndFlush(faculties);
    }
}
