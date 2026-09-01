package ua.com.foxminded.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.ClassroomMapper;
import ua.com.foxminded.repository.ClassroomRepository;

@Service
@Transactional
public class ClassroomService implements FullCrudService<Classroom, ClassroomCreateUpdateDto, Integer> {

    private final ClassroomRepository classroomRapository;
    private final ClassroomMapper classroomMapper;

    public ClassroomService(ClassroomRepository classroomRapository, ClassroomMapper classroomMapper) {
        this.classroomRapository = classroomRapository;
        this.classroomMapper = classroomMapper;
    }

    @Override
    public Classroom addOne(ClassroomCreateUpdateDto dto) {

        Classroom classroom = classroomMapper.toEntity(dto);

        try {
            return classroomRapository.saveAndFlush(classroom);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException("Classroom with title: " + dto.getName() + " alredy exists!");
        }
    }

    @Override
    public Classroom findById(Integer id) {
        return classroomRapository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + id));
    }

    public Classroom findByName(String name) {
        return classroomRapository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Classroom not found: " + name));
    }

    @Override
    public List<Classroom> findAll() {
        return classroomRapository.findAll();
    }

    @Override
    public Classroom fullUpdate(ClassroomCreateUpdateDto updated, Integer id) {
        Classroom classroom = findById(id);
        UpdateUtil.setIfNotNull(updated.getName(), classroom::setName);
        return classroom;
    }

    @Override
    public void deleteById(Integer id) {
        classroomRapository.deleteById(id);
    }

    public void createClassrooms(List<ClassroomCreateUpdateDto> list) {
        List<Classroom> classrooms = list.stream().map(this::addOne).collect(Collectors.toList());
        classroomRapository.saveAllAndFlush(classrooms);
    }

}
