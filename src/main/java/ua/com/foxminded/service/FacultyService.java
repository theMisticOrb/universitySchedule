package ua.com.foxminded.service;

import java.util.List;

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
import ua.com.foxminded.repository.FacultyRepository;

@Service
@Transactional
public class FacultyService {

    private FacultyRepository facultyRepository;
    private AcademicDepartmentMapper departmentMapper;
    private FacultyMapper facultyMapper;

    public FacultyService(FacultyRepository facultyRepository, AcademicDepartmentMapper departmentMapper,
            FacultyMapper facultyMapper) {
        this.facultyRepository = facultyRepository;
        this.departmentMapper = departmentMapper;
        this.facultyMapper = facultyMapper;
    }

    public Faculty addOne(FacultyCreateUpdateDto dto) {

        Faculty faculty = facultyMapper.toEntity(dto);

        try {
            return facultyRepository.save(faculty);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Faculty with title: " + dto.getTitle() + " alredy exists!");

        }
    }

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

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty update(FacultyCreateUpdateDto updated, Integer id) {
        Faculty faculty = findById(id);
        UpdateUtil.updateListIfNotNull(updated.getDepartments(),
                updatedDto -> departmentMapper.toEntity(updatedDto, faculty));
        UpdateUtil.setIfNotNull(updated.getTitle(), faculty::setTitle);
        UpdateUtil.setIfNotNull(updated.getCode(), faculty::setCode);
        return faculty;
    }

    public void deleteById(Integer id) {
        facultyRepository.deleteById(id);
    }

    public long count() {
        return facultyRepository.count();
    }
}
