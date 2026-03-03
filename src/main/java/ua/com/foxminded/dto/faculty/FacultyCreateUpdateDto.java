package ua.com.foxminded.dto.faculty;

import java.util.List;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;

public class FacultyCreateUpdateDto {

    private String title;
    private String code;
    private List<AcademicDepartmentCreateUpdateDto> departments;

    public FacultyCreateUpdateDto() {
    }

    public FacultyCreateUpdateDto(String title, String code, List<AcademicDepartmentCreateUpdateDto> departments) {
        this.title = title;
        this.code = code;
        this.departments = departments;
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

    public List<AcademicDepartmentCreateUpdateDto> getDepartments() {
        return departments;
    }

    public void setDepartments(List<AcademicDepartmentCreateUpdateDto> departments) {
        this.departments = departments;
    }

}
