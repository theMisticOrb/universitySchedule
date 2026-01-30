package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Service
@Transactional
public class FacultyService {

    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addOne(FacultyCreateUpdateDto dto) {
        if (facultyRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateEntityException("Faculty with title: " + dto.getTitle() + " alredy exists!");

        }
        Faculty faculty = new Faculty();
        faculty.setTitle(dto.getTitle());
        return facultyRepository.save(faculty);
    }

    public Faculty findById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found: " + id));
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
        AcademicDepartmentMapper mapper = new AcademicDepartmentMapper();
        UpdateUtil.updateListIfNotNull(updated.getDepartments(), faculty.getDepartments(), mapper::toEntity);
        UpdateUtil.setIfNotNull(updated.getTitle(), faculty::setTitle);
        return faculty;
    }

    public void deleteById(Integer id) {
        facultyRepository.deleteById(id);
    }
}
