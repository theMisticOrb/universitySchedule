package ua.com.foxminded.dto.classroom;

import java.util.List;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;

public class ClassroomResponseDto {

    private Integer id;
    private String name;
    private List<LessonCreateUpdateDto> lessons;

    public ClassroomResponseDto() {
    }

    public ClassroomResponseDto(Integer id, String name, List<LessonCreateUpdateDto> lessons) {
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

    public List<LessonCreateUpdateDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonCreateUpdateDto> lessons) {
        this.lessons = lessons;
    }

}
