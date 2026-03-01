package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.repository.ClassroomRepository;

@Service
@Transactional
public class ClassroomService {

    private ClassroomRepository classroomRapository;

    public ClassroomService(ClassroomRepository classroomRapository) {
        this.classroomRapository = classroomRapository;
    }

    public Classroom addOne(ClassroomCreateUpdateDto dto) {
        if (classroomRapository.existsByName(dto.getName())) {
            throw new DuplicateEntityException("Classroom with title: " + dto.getName() + " alredy exists!");
        }
        Classroom classroom = new Classroom();
        classroom.setName(dto.getName());
        return classroomRapository.save(classroom);
    }

    public Classroom findById(Integer id) {
        return classroomRapository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + id));
    }

    public Classroom findByName(String name) {
        return classroomRapository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + name));
    }

    public List<Classroom> findAll() {
        return classroomRapository.findAll();
    }

    public Classroom update(ClassroomCreateUpdateDto updated, Integer id) {
        Classroom classroom = findById(id);

        LessonMapper mapper = new LessonMapper();
        UpdateUtil.setIfNotNull(updated.getName(), classroom::setName);
        UpdateUtil.updateListIfNotNull(updated.getLessons(), mapper::toEntity);
        return classroom;
    }

    public void deleteById(Integer id) {
        classroomRapository.deleteById(id);
    }

}
