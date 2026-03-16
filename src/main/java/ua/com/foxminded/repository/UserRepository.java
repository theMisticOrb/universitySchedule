package ua.com.foxminded.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer>{

}
