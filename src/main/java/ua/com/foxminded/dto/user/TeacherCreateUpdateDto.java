package ua.com.foxminded.dto.user;

import java.util.List;

public class TeacherCreateUpdateDto extends UserCreateUpdateDto {

    private String departmentCode;
    private List<String> subjects;

    public TeacherCreateUpdateDto() {
    }

    public TeacherCreateUpdateDto(String departmentCode, List<String> subjects) {
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

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

}