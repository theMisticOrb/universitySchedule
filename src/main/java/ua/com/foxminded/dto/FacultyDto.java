package ua.com.foxminded.dto;

import java.util.List;

public class FacultyDto {

    private String title;
    private List<AcademicDepartmentDto> departments;

    public FacultyDto() {
    }

    public FacultyDto(String title, List<AcademicDepartmentDto> departments) {
        this.title = title;
        this.departments = departments;
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
