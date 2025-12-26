package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.helpers.UpdateUtils;
import ua.com.foxminded.sdjpa.SpecializationRepository;

@Service
@Transactional
public class SpecializationService {

    private SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository = specializationRepository;
    }

    public Specialization create(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public Specialization findById(Integer id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Specialization not found: " + id));
    }

    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }

    public Specialization update(Specialization updated) {
        Specialization specialization = findById(updated.getId());
        UpdateUtils.setIfNotNull(updated.getDepartment(), specialization::setDepartment);
        UpdateUtils.setIfNotNull(updated.getGroups(), specialization::setGroups);
        UpdateUtils.setIfNotNull(updated.getTitle(), specialization::setTitle);
        return updated;
    }

    public void deleteById(Integer id) {
        specializationRepository.deleteById(id);
    }

}
