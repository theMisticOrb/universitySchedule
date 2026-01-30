package ua.com.foxminded.dto.classroom;

import java.util.List;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;

public class ClassroomCreateUpdateDto {

    private String name;
    private List<LessonCreateUpdateDto> lessons;

    public ClassroomCreateUpdateDto() {
    }

    public ClassroomCreateUpdateDto(String name, List<LessonCreateUpdateDto> lessons) {
        this.name = name;
        this.lessons = lessons;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LessonCreateUpdateDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonCreateUpdateDto> lessons) {
        this.lessons = lessons;
    }

}
