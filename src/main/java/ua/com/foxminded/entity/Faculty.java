package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_id_gen")
    @SequenceGenerator(name = "faculty_id_gen", sequenceName = "faculty_id_seq", allocationSize = 30)
    @Column(name = "faculty_id")
    private Integer id;

    @Column(name = "faculty_title")
    private String title;

    @OneToMany(mappedBy = "faculty")
    private List<AcademicDepartment> departments = new ArrayList<>();

    public Faculty() {
    }

    public Faculty(Integer id, String title) {
        this.id = id;
        this.title = title;
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

    public List<AcademicDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<AcademicDepartment> departments) {
        this.departments = departments;
    }

}
