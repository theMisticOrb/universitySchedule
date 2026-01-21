package ua.com.foxminded.dto;

import java.time.LocalDate;
import java.util.List;

public class GroupDto {

    private Integer id;
    private String name;
    private Integer specializationId;
    private Integer departmentId;
    private Integer facultyId;
    private LocalDate startDate;
    private List<StudentDto> students;
    private List<SubjectDto> subjects;

    public GroupDto() {
    }

    public GroupDto(Integer id, Integer specializationId, Integer departmentId, Integer facultyId,
            List<StudentDto> students, List<SubjectDto> subjects) {
        this.id = id;
        this.specializationId = specializationId;
        this.departmentId = departmentId;
        this.facultyId = facultyId;
        this.students = students;
        this.subjects = subjects;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public YearOfUnivDto getYearOfUniv() {
        return yearOfUniv;
    }

    public void setYearOfUniv(YearOfUnivDto yearOfUniv) {
        this.yearOfUniv = yearOfUniv;
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
