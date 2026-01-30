package ua.com.foxminded.dto.group;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentCreateUpdateDto;

public class GroupResponseDto {

    private Integer id;
    private String name;

    private Integer facultyId;
    private Integer departmentId;
    private Integer specializationId;

    private LocalDate startDate;
    private Integer currentYearOfUniv;

    private List<StudentCreateUpdateDto> students;
    private List<SubjectCreateUpdateDto> subjects;

    public GroupResponseDto() {
    }

    public GroupResponseDto(Integer id, String name, Integer facultyId, Integer departmentId, Integer specializationId,
            List<StudentCreateUpdateDto> students, List<SubjectCreateUpdateDto> subjects) {
        this.id = id;
        this.name = name;
        this.facultyId = facultyId;
        this.departmentId = departmentId;
        this.specializationId = specializationId;
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

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Integer specializationId) {
        this.specializationId = specializationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getCurrentYearOfUniv() {
        return currentYearOfUniv;
    }

    public void setCurrentYearOfUniv(Integer currentYearOfUniv) {
        this.currentYearOfUniv = currentYearOfUniv;
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
