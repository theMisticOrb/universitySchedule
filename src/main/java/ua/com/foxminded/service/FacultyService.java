package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.FacultyDto;
import ua.com.foxminded.entity.Faculty;
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

    public Faculty addOne(FacultyDto dto) {
        Faculty faculty = new Faculty();
        faculty.setTitle(dto.getTitle());
        return facultyRepository.save(faculty);
    }

    public Faculty findById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found: " + id));
    }

    public Optional<Faculty> findByTitle(String title) {
        return facultyRepository.findByTitle(title);
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty update(FacultyDto updated, Integer id) {
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
