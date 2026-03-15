package ua.com.foxminded.dto.subject;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;

public class SubjectResponseDto {

    private Integer id;
    private String title;
    private Integer teacherId;
    private String teacherFullName;
    private Integer departmentId;
    private List<GroupCreateUpdateDto> groups;
    private List<LessonCreateUpdateDto> lessons;

    public SubjectResponseDto() {
    }

    public SubjectResponseDto(Integer id, String title, Integer teacherId, String teacherFullName, Integer departmentId,
            List<GroupCreateUpdateDto> groups, List<LessonCreateUpdateDto> lessons) {
        this.id = id;
        this.title = title;
        this.teacherId = teacherId;
        this.teacherFullName = teacherFullName;
        this.departmentId = departmentId;
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

    public String getTeacherFullName() {
        return teacherFullName;
    }

    public void setTeacherFullName(String teacherFullName) {
        this.teacherFullName = teacherFullName;
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
