package ua.com.foxminded.dto;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonDto {

    private Integer classroomId;
    private Integer subjectId;
    private ClassHour classHour;
    private WeekDay weekDay;

    public LessonDto() {
    }

    public LessonDto(Integer classroomId, Integer subjectId, ClassHour classHour, WeekDay weekDay) {
        this.classroomId = classroomId;
        this.subjectId = subjectId;
        this.classHour = classHour;
        this.weekDay = weekDay;
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

    

}
