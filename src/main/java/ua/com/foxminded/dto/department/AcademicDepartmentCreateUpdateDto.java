package ua.com.foxminded.dto.department;

import java.util.List;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class AcademicDepartmentCreateUpdateDto {

    private String title;
    private String code;
    private String facultyCode;
    private List<SpecializationCreateUpdateDto> specializations;
    private List<SubjectCreateUpdateDto> subjects;

    public AcademicDepartmentCreateUpdateDto() {
    }

    public AcademicDepartmentCreateUpdateDto(String title, String code, String facultyCode,
            List<SpecializationCreateUpdateDto> specializations, List<SubjectCreateUpdateDto> subjects) {
        this.title = title;
        this.code = code;
        this.facultyCode = facultyCode;
        this.specializations = specializations;
        this.subjects = subjects;
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

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public List<SpecializationCreateUpdateDto> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<SpecializationCreateUpdateDto> specializations) {
        this.specializations = specializations;
    }

    public List<SubjectCreateUpdateDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectCreateUpdateDto> subjects) {
        this.subjects = subjects;
    }

}
