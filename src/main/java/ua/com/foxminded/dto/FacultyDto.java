package ua.com.foxminded.dto;

import java.util.List;

public class FacultyDto {

    private Integer id;
    private String title;
    private List<AcademicDepartmentDto> departments;

    public FacultyDto() {
    }

    public FacultyDto(Integer id, String title, List<AcademicDepartmentDto> departments) {
        this.id = id;
        this.title = title;
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

    public List<AcademicDepartmentDto> getDepartments() {
        return departments;
    }

    public void setDepartments(List<AcademicDepartmentDto> departments) {
        this.departments = departments;
    }

}
