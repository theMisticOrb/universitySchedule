package ua.com.foxminded.dto;

import java.util.List;

public class SubjectDto {

    private Integer id;
    private String title;
    private Integer teacherId;
    private List<GroupDto> groups;
    private List<LessonDto> lessons;

    public SubjectDto() {
    }

    public SubjectDto(Integer id, String title, Integer teacherId, List<GroupDto> groups, List<LessonDto> lessons) {
        this.id = id;
        this.title = title;
        this.teacherId = teacherId;
        this.groups = groups;
        this.lessons = lessons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }

}
