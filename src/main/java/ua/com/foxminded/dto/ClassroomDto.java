package ua.com.foxminded.dto;

import java.util.List;

public class ClassroomDto {

    private Integer id;
    private String name;
    private List<LessonDto> lessons;

    public ClassroomDto() {
    }

    public ClassroomDto(Integer id, String name, List<LessonDto> lessons) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
