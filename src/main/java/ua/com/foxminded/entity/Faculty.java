package ua.com.foxminded.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "faculties")
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Integer id;

    @Column(name = "faculty_title")
    private String title;

    @Column(name = "faculty_code")
    private String code;

    @OneToMany(mappedBy = "faculty")
    private List<AcademicDepartment> departments = new ArrayList<>();

    public Faculty() {
    }

    public Faculty(Integer id, String title, String code, List<AcademicDepartment> departments) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.departments = departments;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<AcademicDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(List<AcademicDepartment> departments) {
        this.departments = departments;
    }

}
