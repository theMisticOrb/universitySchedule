package ua.com.foxminded.enums;

import java.time.LocalTime;

public enum ClassHour {

    FIRST_CLASS(LocalTime.of(8, 30), LocalTime.of(10, 5)), 
    SECOND_CLASS(LocalTime.of(10, 15), LocalTime.of(11, 50)), 
    THIRD_CLASS(LocalTime.of(12, 10), LocalTime.of(13, 45)), 
    FOURTH_CLASS(LocalTime.of(13, 55), LocalTime.of(15, 30)), 
    FIFTH_CLASS(LocalTime.of(15, 50), LocalTime.of(17, 25)),
    SIXTH_CLASS(LocalTime.of(17, 35), LocalTime.of(19, 10)),
    SEVENTH_CLASS(LocalTime.of(19, 20), LocalTime.of(20, 55));
    
    private final LocalTime start;
    private final LocalTime end;
    
    private ClassHour(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
    
    
}
