package ua.com.foxminded.dto.user;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class TeacherCreateUpdateDto extends UserCreateUpdateDto {

    private String departmentCode;
    private List<SubjectCreateUpdateDto> subjects;

    public TeacherCreateUpdateDto() {
    }

    public TeacherCreateUpdateDto(String departmentCode, List<SubjectCreateUpdateDto> subjects) {
        this.departmentCode = departmentCode;
        this.subjects = subjects;
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }

    @Override
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