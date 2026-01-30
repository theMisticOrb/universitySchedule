package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.sdjpa.TeacherRepository;

@Service
@Transactional
public class TeacherService {

    private TeacherRepository teacherRepository;
    private AcademicDepartmentService departmentService;

    public TeacherService(TeacherRepository teacherRepository, AcademicDepartmentService departmentService) {
        this.teacherRepository = teacherRepository;
        this.departmentService = departmentService;
    }

    public Teacher addOne(TeacherCreateUpdateDto dto) {
        Teacher teacher = new Teacher();
        AcademicDepartment department = departmentService.findById(dto.getDepartmentId());

        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setMiddleName(dto.getMiddleName());
        teacher.setDepartment(department);
        return teacherRepository.save(teacher);
    }

    public Teacher findById(Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found: " + id));
    }

    public Teacher findByFullName(String lastName, String firstName, String middleName) {
        return teacherRepository.findByLastNameAndFirstNameAndMiddleName(lastName, firstName, middleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Teacher not found: " + lastName + " " + firstName + " " + middleName));

    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher update(TeacherCreateUpdateDto updated, Integer id) {
        Teacher teacher = findById(id);
        SubjectMapper mapper = new SubjectMapper();

        if (updated.getDepartmentId() != null) {

            AcademicDepartment department = departmentService.findById(updated.getDepartmentId());
            UpdateUtil.setIfNotNull(department, teacher::setDepartment);

        }

        UpdateUtil.setIfNotNull(updated.getFirstName(), teacher::setFirstName);
        UpdateUtil.setIfNotNull(updated.getMiddleName(), teacher::setMiddleName);
        UpdateUtil.setIfNotNull(updated.getLastName(), teacher::setLastName);
        UpdateUtil.updateListIfNotNull(updated.getSubjects(), teacher.getSubjects(), mapper::toEntity);
        return teacher;
    }

    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }

}
