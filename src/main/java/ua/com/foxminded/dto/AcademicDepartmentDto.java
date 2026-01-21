package ua.com.foxminded.dto;

import java.util.List;

public class AcademicDepartmentDto {

    private String title;
    private Integer facultyId;
    private List<SpecializationDto> specializations;

    public AcademicDepartmentDto() {
    }

    public AcademicDepartmentDto(String title, Integer facultyId, List<SpecializationDto> specializations) {
        this.title = title;
        this.facultyId = facultyId;
        this.specializations = specializations;
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

    public List<SpecializationDto> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<SpecializationDto> specializations) {
        this.specializations = specializations;
    }



}
