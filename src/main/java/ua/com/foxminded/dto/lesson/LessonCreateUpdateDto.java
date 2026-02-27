package ua.com.foxminded.dto.lesson;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonCreateUpdateDto {

    private Integer classroomId;
    private Integer subjectId;
    private ClassHour classHour;
    private WeekDay weekDay;
    private Group group;

    public LessonCreateUpdateDto() {
    }

    public LessonCreateUpdateDto(Integer classroomId, Integer subjectId, ClassHour classHour, WeekDay weekDay,
            Group group) {
        this.classroomId = classroomId;
        this.subjectId = subjectId;
        this.classHour = classHour;
        this.weekDay = weekDay;
        this.group = group;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}
