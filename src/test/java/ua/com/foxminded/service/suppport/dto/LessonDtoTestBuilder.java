package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonDtoTestBuilder {

    private String classroomName; // FK — обязателен
    private String subjectTitle; // FK — обязателен
    private String groupName; // FK — обязателен
    private WeekDay weekDay = WeekDay.MONDAY;
    private ClassHour classHour = ClassHour.FIRST_CLASS;

    public static LessonDtoTestBuilder aLesson() {
        return new LessonDtoTestBuilder();
    }

    public LessonDtoTestBuilder withClassroomName(String classroomName) {
        this.classroomName = classroomName;
        return this;
    }

    public LessonDtoTestBuilder withSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
        return this;
    }

    public LessonDtoTestBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public LessonDtoTestBuilder withWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
        return this;
    }

    public LessonDtoTestBuilder withClassHour(ClassHour classHour) {
        this.classHour = classHour;
        return this;
    }

    public LessonCreateUpdateDto build() {
        if (classroomName == null || subjectTitle == null || groupName == null) {
            throw new IllegalStateException(
                    "classroomName, subjectTitle and groupName are required — set them before build()");
        }
        LessonCreateUpdateDto dto = new LessonCreateUpdateDto();
        dto.setClassroomName(classroomName);
        dto.setSubjectTitle(subjectTitle);
        dto.setGroupName(groupName);
        dto.setWeekDay(weekDay);
        dto.setClassHour(classHour);
        return dto;
    }
}