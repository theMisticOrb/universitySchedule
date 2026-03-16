package ua.com.foxminded.dto;

import java.time.LocalDate;
import java.util.List;

import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Lesson;

public class DaySchedule {

    private Group group;
    private LocalDate date;
    private List<Lesson> lessons;

    public DaySchedule() {
    }

    public DaySchedule(Group group, LocalDate date, List<Lesson> lessons) {
        this.group = group;
        this.date = date;
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

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }


}
