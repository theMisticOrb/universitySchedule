package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "specializations")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "specialization_id")
    private Integer id;

    @Column(name = "specialization_title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private AcademicDepartment department;

    @OneToMany(mappedBy = "specialization")
    private List<Group> groups = new ArrayList<>();

    public Specialization() {
    }

    public Specialization(Integer id, String title, AcademicDepartment department, List<Group> groups) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.groups = groups;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AcademicDepartment getDepartment() {
        return department;
    }

    public void setDepartment(AcademicDepartment department) {
        this.department = department;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
