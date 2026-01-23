package ua.com.foxminded.dto.groupdto;

import java.util.List;

import ua.com.foxminded.dto.StudentDto;
import ua.com.foxminded.dto.SubjectDto;

public class GroupCreateUpdateDto {

    private String name;

    private Integer specializationId;
    private Integer departmentId;
    private Integer facultyId;

    private List<StudentDto> students;
    private List<SubjectDto> subjects;

    public GroupCreateUpdateDto() {
    }

    public GroupCreateUpdateDto(String name, Integer specializationId, Integer departmentId, Integer facultyId,
            List<StudentDto> students, List<SubjectDto> subjects) {
        this.name = name;
        this.specializationId = specializationId;
        this.departmentId = departmentId;
        this.facultyId = facultyId;
        this.students = students;
        this.subjects = subjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<StudentDto> getStudents() {
        return students;
    }

    public void setStudents(List<StudentDto> students) {
        this.students = students;
    }

    public List<SubjectDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectDto> subjects) {
        this.subjects = subjects;
    }

}
