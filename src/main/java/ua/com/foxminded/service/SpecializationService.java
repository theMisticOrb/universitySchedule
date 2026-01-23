package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.SpecializationDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.sdjpa.SpecializationRepository;

@Service
@Transactional
public class SpecializationService {

    private SpecializationRepository specializationRepository;
    private AcademicDepartmentService departmentService;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public Specialization addOne(SpecializationDto dto) {
        Specialization specialization = new Specialization();

        AcademicDepartment department = new AcademicDepartment();
        department.setId(dto.getDepartmentId());

        specialization.setDepartment(department);
        specialization.setTitle(dto.getTitle());

        return specializationRepository.save(specialization);
    }

    public Specialization findById(Integer id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found: " + id));
    }

    public Optional<Specialization> findByTitle(String title) {
        return specializationRepository.findByTitle(title);
    }

    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    public Specialization update(SpecializationDto updated, Integer id) {
        Specialization specialization = findById(id);
        AcademicDepartment department = departmentService.findById(updated.getDepartmentId());

        GroupMapper mapper = new GroupMapper();
        
        UpdateUtil.setIfNotNull(department, specialization::setDepartment);
        UpdateUtil.updateListIfNotNull(updated.getGroups(), specialization.getGroups(), mapper::toEntity);
        UpdateUtil.setIfNotNull(updated.getTitle(), specialization::setTitle);
        return specialization;
    }

    public void deleteById(Integer id) {
        specializationRepository.deleteById(id);
    }

}
