package ua.com.foxminded.dto;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.enums.ClassHour;

public class DaySchedule {

    private Group group;
    private LocalDate date;
    private ClassHour classHour;
    private List<Lesson> lessons;

    public DaySchedule() {
    }

    public DaySchedule(Group group, LocalDate date, ClassHour classHour, List<Lesson> lessons) {
        this.group = group;
        this.date = date;
        this.classHour = classHour;
        this.lessons = lessons;
    }

    public Group getGroup() {
        return group;
    }

    public LocalDate getDate() {
        return date;
    }

    public ClassHour getClassHour() {
        return classHour;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

}
