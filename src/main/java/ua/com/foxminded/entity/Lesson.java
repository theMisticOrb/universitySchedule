package ua.com.foxminded.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_hours")
    private ClassHour classHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_days")
    private WeekDay weekDays;

    public Lesson() {
    }

    public Lesson(Subject subject, Classroom classroom, ClassHour classHours) {
        this.subject = subject;
        this.classroom = classroom;
        this.classHours = classHours;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WeekDay getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(WeekDay weekDays) {
        this.weekDays = weekDays;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public ClassHour getClassHours() {
        return classHours;
    }

    public void setClassHours(ClassHour classHours) {
        this.classHours = classHours;
    }

}
