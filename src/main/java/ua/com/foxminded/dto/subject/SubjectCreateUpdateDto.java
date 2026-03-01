package ua.com.foxminded.dto.subject;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;

public class SubjectCreateUpdateDto {

    private String title;
    private Integer teacherId;
    private Integer departmentId;
    private List<GroupCreateUpdateDto> groups;
    private List<LessonCreateUpdateDto> lessons;

    public SubjectCreateUpdateDto() {
    }

    public SubjectCreateUpdateDto(String title, Integer teacherId, Integer departmentId,
            List<GroupCreateUpdateDto> groups, List<LessonCreateUpdateDto> lessons) {
        this.title = title;
        this.teacherId = teacherId;
        this.departmentId = departmentId;
        this.groups = groups;
        this.lessons = lessons;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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

}
