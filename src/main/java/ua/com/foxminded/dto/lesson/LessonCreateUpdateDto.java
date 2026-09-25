package ua.com.foxminded.dto.lesson;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonCreateUpdateDto {

    private String classroomName;
    private Integer subjectId;
    private ClassHour classHour;
    private WeekDay weekDay;
    private Integer groupId;

    public LessonCreateUpdateDto() {
    }

    public LessonCreateUpdateDto(String classroomName, Integer subjectId, ClassHour classHour, WeekDay weekDay,
            Integer groupId) {
        this.classroomName = classroomName;
        this.subjectId = subjectId;
        this.classHour = classHour;
        this.weekDay = weekDay;
        this.groupId = groupId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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

}
