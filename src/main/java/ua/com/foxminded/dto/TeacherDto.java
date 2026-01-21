package ua.com.foxminded.dto;

import java.util.List;

public class TeacherDto extends UserDto {

    private Integer departmentId;
    private List<SubjectDto> subjects;

    public TeacherDto() {
    }

    public TeacherDto(Integer departmentId, List<SubjectDto> subjects) {
        this.departmentId = departmentId;
        this.subjects = subjects;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<SubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDto> subjects) {
        this.subjects = subjects;
    }

}