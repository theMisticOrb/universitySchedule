package ua.com.foxminded.dto.subject;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class AssignSubjectDto {

    @NotEmpty(message = "subjectTitles should not be emptys")
    private List<String> subjectTitles;

    public AssignSubjectDto() {
    }

    public AssignSubjectDto(List<String> subjectTitles) {
        this.subjectTitles = subjectTitles;
    }

    public List<String> getSubjectTitles() {
        return subjectTitles;
    }

    public void setSubjectTitles(List<String> subjectTitles) {
        this.subjectTitles = subjectTitles;
    }
}
