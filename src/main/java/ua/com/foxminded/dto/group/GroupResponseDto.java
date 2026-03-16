package ua.com.foxminded.dto.group;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentCreateUpdateDto;

public class GroupResponseDto {

    private Integer id;
    private String name;

    private String specializationCode;

    private LocalDate startDate;
    private Integer currentYearOfUniv;

    private List<StudentCreateUpdateDto> students;
    private List<SubjectCreateUpdateDto> subjects;

    public GroupResponseDto() {
    }

    public GroupResponseDto(Integer id, String name, String specializationCode, LocalDate startDate,
            Integer currentYearOfUniv, List<StudentCreateUpdateDto> students, List<SubjectCreateUpdateDto> subjects) {
        this.id = id;
        this.name = name;
        this.specializationCode = specializationCode;
        this.startDate = startDate;
        this.currentYearOfUniv = currentYearOfUniv;
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

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String string) {
        this.specializationCode = string;
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
