package ua.com.foxminded.dto.department;

import java.util.List;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class AcademicDepartmentResponseDto {

    private Integer id;
    private String title;
    private String code;
    private String facultyCode;
    private List<SpecializationCreateUpdateDto> specializations;
    private List<SubjectCreateUpdateDto> subject;

    public AcademicDepartmentResponseDto() {
    }

    public AcademicDepartmentResponseDto(Integer id, String title, String code, String facultyCode,
            List<SpecializationCreateUpdateDto> specializations, List<SubjectCreateUpdateDto> subject) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.facultyCode = facultyCode;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String string) {
        this.facultyCode = string;
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
