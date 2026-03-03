package ua.com.foxminded.dto.department;

import java.util.List;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class AcademicDepartmentCreateUpdateDto {

    private String title;
    private String code;
    private Integer facultyId;
    private List<SpecializationCreateUpdateDto> specializations;
    private List<SubjectCreateUpdateDto> subjects;

    public AcademicDepartmentCreateUpdateDto() {
    }

    public AcademicDepartmentCreateUpdateDto(String title, String code, Integer facultyId,
            List<SpecializationCreateUpdateDto> specializations, List<SubjectCreateUpdateDto> subjects) {
        this.title = title;
        this.code = code;
        this.facultyId = facultyId;
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

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
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
