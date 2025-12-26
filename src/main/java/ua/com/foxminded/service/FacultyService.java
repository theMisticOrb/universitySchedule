package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.helpers.UpdateUtils;
import ua.com.foxminded.sdjpa.FacultyRepository;

@Service
@Transactional
public class FacultyService {

    private FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }
    
    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findById(Integer id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Faculty not found: " + id));
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty update(Faculty updated) {
        Faculty faculty = findById(updated.getId());
        UpdateUtils.setIfNotNull(updated.getDepartments(), faculty::setDepartments);
        UpdateUtils.setIfNotNull(updated.getTitle(), faculty::setTitle);
        return updated;
    }

    public void deleteById(Integer id) {
        facultyRepository.deleteById(id);
    }
}
