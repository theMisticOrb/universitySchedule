package ua.com.foxminded.service;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User are not found by this email: " + email));
    }

    public AppUser findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User are not found by this id: " + id));
    }

    public void changeRole(Integer id, Set<UserRole> newRoles) {
        AppUser user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRoles(newRoles);
        userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public long count() {
        return userRepository.count();
    }
}
