package ua.com.foxminded.dto.user;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;

public class TeacherResponseDto extends UserResponseDto {

    private Integer departmentId;
    private String departmentCode;
    private List<SubjectCreateUpdateDto> subjects;

    public TeacherResponseDto() {
    }

    public TeacherResponseDto(Integer id, String firstName, String middleName, String lastName, Integer departmentId,
            String departmentCode, List<SubjectCreateUpdateDto> subjects) {
        super(id, firstName, middleName, lastName);
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
