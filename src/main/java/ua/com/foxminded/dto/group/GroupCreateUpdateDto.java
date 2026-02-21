package ua.com.foxminded.dto.group;

import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentCreateUpdateDto;

public class GroupCreateUpdateDto {

    private Integer specializationId;

    private List<StudentCreateUpdateDto> students;
    private List<SubjectCreateUpdateDto> subjects;

    public GroupCreateUpdateDto() {
    }

    public GroupCreateUpdateDto(Integer specializationId, List<StudentCreateUpdateDto> students,
            List<SubjectCreateUpdateDto> subjects) {
        this.specializationId = specializationId;
        this.students = students;
        this.subjects = subjects;
    }

    public Integer getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(Integer specializationId) {
        this.specializationId = specializationId;
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
