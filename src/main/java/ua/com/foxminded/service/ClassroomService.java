package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.ClassroomDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.sdjpa.ClassroomRepository;

@Service
@Transactional
public class ClassroomService {

    private ClassroomRepository classroomRapository;

    public ClassroomService(ClassroomRepository classroomRapository) {
        this.classroomRapository = classroomRapository;
    }

    public Classroom addOne(ClassroomDto dto) {
        Classroom classroom = new Classroom();
        classroom.setName(dto.getName());
        return classroomRapository.save(classroom);
    }

    public Classroom findById(Integer id) {
        return classroomRapository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + id));
    }

    public Optional<Classroom> findByTitle(String name) {
        return classroomRapository.findByName(name);
    }

    public List<Classroom> findAll() {
        return classroomRapository.findAll();
    }

    public Classroom update(ClassroomDto updated, Integer id) {
        Classroom classroom = findById(id);

        LessonMapper mapper = new LessonMapper();
        UpdateUtil.setIfNotNull(updated.getName(), classroom::setName);
        UpdateUtil.updateListIfNotNull(updated.getLessons(), classroom.getLesson(), mapper::toEntity);
        return classroom;
    }

    public void deleteById(Integer id) {
        classroomRapository.deleteById(id);
    }

}
