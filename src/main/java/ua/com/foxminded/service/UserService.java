package ua.com.foxminded.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.user.UserCreateUpdateDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private GroupRepository groupRepositoty;
    private AcademicDepartmentRepository departmentRepositoty;
    private ActivationUserService activationUserService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GroupRepository groupRepositoty,
            AcademicDepartmentRepository departmentRepositoty, ActivationUserService activationUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.groupRepositoty = groupRepositoty;
        this.departmentRepositoty = departmentRepositoty;
        this.activationUserService = activationUserService;
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

    public void createUsers(List<UserCreateUpdateDto> usersList) {
        List<AppUser> users = usersList.stream().map(this::buildUser).collect(Collectors.toList());
        userRepository.saveAllAndFlush(users);
        users.forEach(activationUserService::sendResetEmailIfTempPassword);

    }

    public AppUser buildUser(UserCreateUpdateDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("User with that email: " + dto.getEmail() + " alredy exists.");
        }
        Set<UserRole> roles = dto.getRoles();

        if (roles.contains(UserRole.STUDENT) && roles.contains(UserRole.TEACHER)) {
            throw new IllegalArgumentException("User cannot be both student and teacher");
        }

        if (roles.contains(UserRole.ADMIN) && !roles.contains(UserRole.TEACHER)) {
            throw new IllegalArgumentException("Admin must also have teacher role");
        }

        AppUser user;

        if (roles.contains(UserRole.STUDENT)) {
            user = buildStudent(dto);
        } else if (roles.contains(UserRole.TEACHER)) {
            user = buildTeacher(dto);
        } else {
            throw new IllegalArgumentException("User type is not specified");
        }

        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRegistrationCompleted(false);
        user.getRoles().addAll(dto.getRoles());
        return user;

    }

    private Student buildStudent(UserCreateUpdateDto dto) {
        Group group = groupRepositoty.findByName(dto.getGroupName())
                .orElseThrow(() -> new IllegalArgumentException("Group not found!"));
        Student student = new Student();
        student.setGroup(group);
        return student;

    }

    private Teacher buildTeacher(UserCreateUpdateDto dto) {
        AcademicDepartment department = departmentRepositoty.findByCode(dto.getDepartmentCode())
                .orElseThrow(() -> new IllegalArgumentException("Department not found!"));
        Teacher teacher = new Teacher();
        teacher.setDepartment(department);
        return teacher;
    }

}
