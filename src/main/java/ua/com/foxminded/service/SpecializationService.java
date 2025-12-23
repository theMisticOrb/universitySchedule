package ua.com.foxminded.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.Specialization;
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

    public Specialization update(Integer id, Specialization updated) {
        Specialization specialization = findById(id);
        specialization.setDepartment(updated.getDepartment());
        specialization.setGroups(updated.getGroups());
        specialization.setTitle(updated.getTitle());
        return updated;
    }

    public void deleteById(Integer id) {
        specializationRepository.deleteById(id);
    }

}
