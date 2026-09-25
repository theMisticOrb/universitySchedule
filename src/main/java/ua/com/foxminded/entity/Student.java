package ua.com.foxminded.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import ua.com.foxminded.enums.UserRole;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends AppUser {

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    public Student() {
    }

    public Student(Integer id, String firstName, String middleName, String lastName, String email, String password,
            LocalDateTime createdAt, Set<UserRole> roles, boolean isRegistrationCompleted, Group group) {
        super(id, firstName, middleName, lastName, email, password, createdAt, roles, isRegistrationCompleted);
        this.group = group;
    }

    public Student(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
