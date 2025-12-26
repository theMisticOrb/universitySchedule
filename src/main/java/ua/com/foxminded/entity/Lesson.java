package ua.com.foxminded.dto;

import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Subject;

public class Lesson {

    private Subject subject;
    private Classroom classroom;
    private ClassHours classHours;

    public Lesson() {
    }

    public Lesson(Subject subject, Classroom classroom, ClassHours classHours) {
        this.subject = subject;
        this.classroom = classroom;
        this.classHours = classHours;
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

    public ClassHours getClassHours() {
        return classHours;
    }

    public void setClassHours(ClassHours classHours) {
        this.classHours = classHours;
    }
    
    

}
