package ua.com.foxminded.dto.group;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentCreateUpdateDto;

public class GroupCreateUpdateDto {

    private Integer specializationId;
    private Integer departmentId;
    private Integer facultyId;

    private List<StudentCreateUpdateDto> students;
    private List<SubjectCreateUpdateDto> subjects;

    public GroupCreateUpdateDto() {
    }

    public GroupCreateUpdateDto(Integer specializationId, Integer departmentId, Integer facultyId,
            List<StudentCreateUpdateDto> students, List<SubjectCreateUpdateDto> subjects) {
        this.specializationId = specializationId;
        this.departmentId = departmentId;
        this.facultyId = facultyId;
        this.students = students;
        this.subjects = subjects;
    }

    public Integer getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Integer specializationId) {
        this.specializationId = specializationId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public List<StudentCreateUpdateDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentCreateUpdateDto> students) {
        this.students = students;
    }

    public List<SubjectCreateUpdateDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectCreateUpdateDto> subjects) {
        this.subjects = subjects;
    }

}
