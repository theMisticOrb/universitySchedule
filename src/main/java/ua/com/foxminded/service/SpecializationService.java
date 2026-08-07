package ua.com.foxminded.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.exception.DuplicateEntityException;
import ua.com.foxminded.helpers.UpdateUtil;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.repository.SpecializationRepository;

@Service
@Transactional
public class SpecializationService {

    private SpecializationRepository specializationRepository;
    private AcademicDepartmentService departmentService;
    private GroupMapper groupMapper;
    private SpecializationMapper specializationMapper;

    public SpecializationService(SpecializationRepository specializationRepository,
            AcademicDepartmentService departmentService, GroupMapper groupMapper,
            SpecializationMapper specializationMapper) {
        this.specializationRepository = specializationRepository;
        this.departmentService = departmentService;
        this.groupMapper = groupMapper;
        this.specializationMapper = specializationMapper;
    }

    public Specialization addOne(SpecializationCreateUpdateDto dto) {

        AcademicDepartment department = departmentService.findByCode(dto.getDepartmentCode());
        Specialization specialization = specializationMapper.toEntity(dto, department);

        try {
            return specializationRepository.save(specialization);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityException(
                    "Specialization with title: " + dto.getTitle() + " alredy exists in this academic department!");

        }

    }

    public Specialization findById(Integer id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found by id: " + id));
    }

    public Specialization findByTitle(String title) {
        return specializationRepository.findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found by title: " + title));
    }

    public Specialization findByCode(String code) {
        return specializationRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found by code: " + code));
    }

    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    public Specialization update(SpecializationCreateUpdateDto updated, Integer id) {

        Specialization specialization = findById(id);

        if (updated.getDepartmentCode() != null) {
            AcademicDepartment department = departmentService.findByCode(updated.getDepartmentCode());
            UpdateUtil.setIfNotNull(department, specialization::setDepartment);
        }

        UpdateUtil.updateListIfNotNull(updated.getGroups(), dto -> groupMapper.toEntity(dto, specialization));
        UpdateUtil.setIfNotNull(updated.getTitle(), specialization::setTitle);
        UpdateUtil.setIfNotNull(updated.getCode(), specialization::setCode);
        return specialization;
    }

    public void deleteById(Integer id) {
        specializationRepository.deleteById(id);
    }

    public void createSpecializations(List<SpecializationCreateUpdateDto> list) {

        List<Specialization> specializations = list.stream().map(this::addOne).collect(Collectors.toList());
        specializationRepository.saveAllAndFlush(specializations);
    }

}
