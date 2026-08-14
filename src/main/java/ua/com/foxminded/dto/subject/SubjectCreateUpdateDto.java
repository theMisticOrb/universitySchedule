package ua.com.foxminded.dto.subject;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;

public class SubjectCreateUpdateDto {

    private String title;
    private List<String> teacherFullNames;
    private List<String> teacherEmails;
    private String departmentCode;
    private List<GroupCreateUpdateDto> groups;
    private List<LessonCreateUpdateDto> lessons;

    public SubjectCreateUpdateDto() {
    }

    public SubjectCreateUpdateDto(String title, List<String> teacherFullNames, List<String> teacherEmails,
            String departmentCode, List<GroupCreateUpdateDto> groups, List<LessonCreateUpdateDto> lessons) {
        this.title = title;
        this.teacherFullNames = teacherFullNames;
        this.teacherEmails = teacherEmails;
        this.departmentCode = departmentCode;
        this.groups = groups;
        this.lessons = lessons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTeacherFullNames() {
        return teacherFullNames;
    }

    public void setTeacherFullNames(List<String> teacherFullName) {
        this.teacherFullNames = teacherFullName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public List<GroupCreateUpdateDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupCreateUpdateDto> groups) {
        this.groups = groups;
    }

    public List<LessonCreateUpdateDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonCreateUpdateDto> lessons) {
        this.lessons = lessons;
    }

    public List<String> getTeacherEmails() {
        return teacherEmails;
    }

    public void setTeacherEmails(List<String> teacherEmail) {
        this.teacherEmails = teacherEmail;
    }

}
