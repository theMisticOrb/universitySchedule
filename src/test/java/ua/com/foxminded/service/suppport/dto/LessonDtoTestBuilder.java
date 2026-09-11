package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;

public class LessonDtoTestBuilder {

    private String classroomName; 
    private Integer subjectId; 
    private Integer groupId; 
    private WeekDay weekDay = WeekDay.MONDAY;
    private ClassHour classHour = ClassHour.FIRST_CLASS;

    public static LessonDtoTestBuilder aLesson() {
        return new LessonDtoTestBuilder();
    }

    public LessonDtoTestBuilder withClassroomName(String classroomName) {
        this.classroomName = classroomName;
        return this;
    }

    public LessonDtoTestBuilder withSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
        return this;
    }

    public LessonDtoTestBuilder withGroupId(Integer groupId) {
        this.groupId = groupId;
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
        if (classroomName == null || subjectId == null || groupId == null) {
            throw new IllegalStateException(
                    "classroomName, subjectTitle and groupName are required — set them before build()");
        }
        LessonCreateUpdateDto dto = new LessonCreateUpdateDto();
        dto.setClassroomName(classroomName);
        dto.setSubjectId(subjectId);
        dto.setGroupId(groupId);
        dto.setWeekDay(weekDay);
        dto.setClassHour(classHour);
        return dto;
    }
}