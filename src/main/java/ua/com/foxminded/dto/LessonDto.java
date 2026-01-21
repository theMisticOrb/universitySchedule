package ua.com.foxminded.dto;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonDto {

    private Integer id;
    private Integer classroomId;
    private ClassHour classHour;
    private WeekDay weekDay;

    public LessonDto() {
    }

    public LessonDto(Integer id, Integer classroomId, ClassHour classHour, WeekDay weekDay) {
        this.id = id;
        this.classroomId = classroomId;
        this.classHour = classHour;
        this.weekDay = weekDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Integer classroomId) {
        this.classroomId = classroomId;
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
