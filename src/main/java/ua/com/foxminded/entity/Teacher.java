package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import ua.com.foxminded.enums.UserRole;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends AppUser {

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private AcademicDepartment department;

    @OneToMany(mappedBy = "teacher")
    private List<Subject> subjects = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(Integer id, String firstName, String middleName, String lastName, String email, String password,
            Set<UserRole> roles, AcademicDepartment department, List<Subject> subjects) {
        super(id, firstName, middleName, lastName, email, password, roles);
        this.department = department;
        this.subjects = subjects;
    }

    public AcademicDepartment getDepartment() {
        return department;
    }

    public void setDepartment(AcademicDepartment department) {
        this.department = department;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

}
