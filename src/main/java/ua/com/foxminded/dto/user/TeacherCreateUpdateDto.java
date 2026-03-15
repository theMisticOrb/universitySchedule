package ua.com.foxminded.dto.user;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class TeacherCreateUpdateDto extends UserCreateUpdateDto {

    private Integer departmentId;
    private String departmentCode;
    private List<SubjectCreateUpdateDto> subjects;

    public TeacherCreateUpdateDto() {
    }

    public TeacherCreateUpdateDto(Integer departmentId, String departmentCode, List<SubjectCreateUpdateDto> subjects) {
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.subjects = subjects;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public List<SubjectCreateUpdateDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectCreateUpdateDto> subjects) {
        this.subjects = subjects;
    }

}