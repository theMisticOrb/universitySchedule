package ua.com.foxminded.dto.subject;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;

public class SubjectCreateUpdateDto {

    private String title;
    private String teacherFullName;
    private String teacherEmail;
    private String departmentCode;
    private List<GroupCreateUpdateDto> groups;
    private List<LessonCreateUpdateDto> lessons;

    public SubjectCreateUpdateDto() {
    }

    public SubjectCreateUpdateDto(String title, String teacherFullName, String teacherEmail, String departmentCode,
            List<GroupCreateUpdateDto> groups, List<LessonCreateUpdateDto> lessons) {
        this.title = title;
        this.teacherFullName = teacherFullName;
        this.teacherEmail = teacherEmail;
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

    public String getTeacherFullName() {
        return teacherFullName;
    }

    public void setTeacherFullName(String teacherFullName) {
        this.teacherFullName = teacherFullName;
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

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }
    
    

}
