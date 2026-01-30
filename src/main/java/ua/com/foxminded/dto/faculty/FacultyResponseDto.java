package ua.com.foxminded.dto.faculty;

import java.util.List;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;

public class FacultyResponseDto {

    private Integer id;
    private String title;
    private List<AcademicDepartmentCreateUpdateDto> departments;

    public FacultyResponseDto() {
    }

    public FacultyResponseDto(Integer id, String title, List<AcademicDepartmentCreateUpdateDto> departments) {
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

    public List<AcademicDepartmentCreateUpdateDto> getDepartments() {
        return departments;
    }

    public void setDepartments(List<AcademicDepartmentCreateUpdateDto> departments) {
        this.departments = departments;
    }

}
