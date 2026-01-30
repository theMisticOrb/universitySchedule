package ua.com.foxminded.dto.user;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class TeacherResponseDto extends UserResponseDto {

    private Integer departmentId;
    private List<SubjectCreateUpdateDto> subjects;

    public TeacherResponseDto() {
    }

    public TeacherResponseDto(Integer departmentId, List<SubjectCreateUpdateDto> subjects) {
        this.departmentId = departmentId;
        this.subjects = subjects;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<SubjectCreateUpdateDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectCreateUpdateDto> subjects) {
        this.subjects = subjects;
    }

}
