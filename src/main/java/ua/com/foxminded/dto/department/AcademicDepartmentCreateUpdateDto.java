package ua.com.foxminded.dto.department;

import java.util.List;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class AcademicDepartmentCreateUpdateDto {

    private String title;
    private Integer facultyId;
    private List<SpecializationCreateUpdateDto> specializations;
    private List<SubjectCreateUpdateDto> subjects;

    public AcademicDepartmentCreateUpdateDto() {
    }

    public AcademicDepartmentCreateUpdateDto(String title, Integer facultyId,
            List<SpecializationCreateUpdateDto> specializations, List<SubjectCreateUpdateDto> subjects) {
        this.title = title;
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
