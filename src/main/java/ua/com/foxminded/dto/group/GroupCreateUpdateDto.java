package ua.com.foxminded.dto.group;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentCreateUpdateDto;

public class GroupCreateUpdateDto {

    private String specializationCode;

    private List<StudentCreateUpdateDto> students;
    private List<SubjectCreateUpdateDto> subjects;

    public GroupCreateUpdateDto() {
    }

    public GroupCreateUpdateDto(String specializationCode, List<StudentCreateUpdateDto> students,
            List<SubjectCreateUpdateDto> subjects) {
        this.specializationCode = specializationCode;
        this.students = students;
        this.subjects = subjects;
    }

    public String getSpecializationCode() {
        return specializationCode;
    }

    public void setSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
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
