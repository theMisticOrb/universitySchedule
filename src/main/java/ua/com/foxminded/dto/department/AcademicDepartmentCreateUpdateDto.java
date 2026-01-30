package ua.com.foxminded.dto.department;

import java.util.List;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;

public class AcademicDepartmentCreateUpdateDto {

    private String title;
    private Integer facultyId;
    private List<SpecializationCreateUpdateDto> specializations;

    public AcademicDepartmentCreateUpdateDto() {
    }

    public AcademicDepartmentCreateUpdateDto(String title, Integer facultyId, List<SpecializationCreateUpdateDto> specializations) {
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

    public List<SpecializationCreateUpdateDto> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<SpecializationCreateUpdateDto> specializations) {
        this.specializations = specializations;
    }



}
