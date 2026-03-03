package ua.com.foxminded.dto.lesson;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonResponseDto {

    private Integer id;
    private String classroomName;
    private String subjectTitle;
    private ClassHour classHour;
    private WeekDay weekDay;
    private Integer groupId;

    public LessonResponseDto() {
    }

    public LessonResponseDto(Integer id, String classroomName, String subjectTitle, ClassHour classHour,
            WeekDay weekDay, Integer groupId) {
        this.id = id;
        this.classroomName = classroomName;
        this.subjectTitle = subjectTitle;
        this.classHour = classHour;
        this.weekDay = weekDay;
        this.groupId = groupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public ClassHour getClassHour() {
        return classHour;
    }

    public void setClassHour(ClassHour classHour) {
        this.classHour = classHour;
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
