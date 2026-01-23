package ua.com.foxminded.dto;

import java.util.List;

public class ClassroomDto {

    private String name;
    private List<LessonDto> lessons;

    public ClassroomDto() {
    }

    public ClassroomDto(String name, List<LessonDto> lessons) {
        this.name = name;
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }

}
