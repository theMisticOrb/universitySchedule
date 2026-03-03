package ua.com.foxminded.dto.lesson;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonCreateUpdateDto {

    private Integer classroomId;
    private Integer subjectId;
    private ClassHour classHour;
    private WeekDay weekDay;
    private Integer groupId;
    private Integer teacherId;

    public LessonCreateUpdateDto() {
    }

    public LessonCreateUpdateDto(Integer classroomId, Integer subjectId, ClassHour classHour, WeekDay weekDay,
            Integer groupId, Integer teacherId) {
        this.classroomId = classroomId;
        this.subjectId = subjectId;
        this.classHour = classHour;
        this.weekDay = weekDay;
        this.groupId = groupId;
        this.teacherId = teacherId;
    }

    public Integer getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Integer classroomId) {
        this.classroomId = classroomId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

}
