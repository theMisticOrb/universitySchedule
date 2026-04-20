package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.AppUser;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<AppUser, Integer>{

    Optional<AppUser> findByEmail(String email);
}
