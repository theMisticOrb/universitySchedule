package ua.com.foxminded.dto.department;

import java.util.List;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class AcademicDepartmentResponseDto {

    private Integer id;
    private String title;
    private Integer facultyId;
    private List<SpecializationCreateUpdateDto> specializations;
    private List<SubjectCreateUpdateDto> subject;

    public AcademicDepartmentResponseDto() {
    }

    public AcademicDepartmentResponseDto(Integer id, String title, Integer facultyId,
            List<SpecializationCreateUpdateDto> specializations, List<SubjectCreateUpdateDto> subject) {
        this.id = id;
        this.title = title;
        this.facultyId = facultyId;
        this.specializations = specializations;
        this.subject = subject;
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

    public List<SubjectCreateUpdateDto> getSubject() {
        return subject;
    }

    public void setSubject(List<SubjectCreateUpdateDto> subject) {
        this.subject = subject;
    }

}
