package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Teacher extends AppUser {

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private AcademicDepartment department;

    @ManyToMany
    @JoinTable(name = "teacher_subjects", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(AcademicDepartment department, List<Subject> subjects) {
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
