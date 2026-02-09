package ua.com.foxminded.dto.faculty;

import java.util.List;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;

public class FacultyCreateUpdateDto {

    private String title;
    private List<AcademicDepartmentCreateUpdateDto> departments;

    public FacultyCreateUpdateDto() {
    }

    public FacultyCreateUpdateDto(String title, List<AcademicDepartmentCreateUpdateDto> departments) {
        this.title = title;
        this.departments = departments;
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
