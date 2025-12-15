package ua.com.foxminded.dto;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.entity.Group;

public class DaySchedule {

    private Group group;
    private LocalDate date;
    private ClassHours classHour;
    private List<Lesson> lessons;

    public DaySchedule() {
    }

    public DaySchedule(Group group, LocalDate date, ClassHours classHour, List<Lesson> lessons) {
        this.group = group;
        this.date = date;
        this.classHour = classHour;
        this.lessons = lessons;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ClassHours getClassHour() {
        return classHour;
    }

    public void setClassHour(ClassHours classHour) {
        this.classHour = classHour;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

}
