package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.sdjpa.ClassroomRepository;

@Service
@Transactional
public class ClassroomService {

    private ClassroomRepository classroomRapository;

    public ClassroomService(ClassroomRepository classroomRapository) {
        this.classroomRapository = classroomRapository;
    }

    public Classroom create(Classroom classroom) {
        return classroomRapository.save(classroom);
    }

    public Classroom findById(Integer id) {
        return classroomRapository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + id));
    }

    public List<Classroom> findAll() {
        return classroomRapository.findAll();
    }

    public Classroom update(Integer id, Classroom updated) {
        Classroom classroom = findById(id);
        classroom.setName(updated.getName());
        return updated;
    }

    public void deleteById(Integer id) {
        classroomRapository.deleteById(id);
    }

}
