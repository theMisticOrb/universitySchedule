package ua.com.foxminded.dto.lesson;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonCreateUpdateDto {

    private String classroomName;
    private String subjectName;
    private ClassHour classHour;
    private WeekDay weekDay;
    private String groupName;

    public LessonCreateUpdateDto() {
    }

    public LessonCreateUpdateDto(String classroomName, String subjectName, ClassHour classHour, WeekDay weekDay,
            String groupName) {
        this.classroomName = classroomName;
        this.subjectName = subjectName;
        this.classHour = classHour;
        this.weekDay = weekDay;
        this.groupName = groupName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
