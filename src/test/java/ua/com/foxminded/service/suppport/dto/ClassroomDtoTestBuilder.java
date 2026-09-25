package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class ClassroomDtoTestBuilder {

    private String name = TestDataFactory.uniqueName("Room");

    public static ClassroomDtoTestBuilder aClassroom() {
        return new ClassroomDtoTestBuilder();
    }

    public ClassroomDtoTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClassroomCreateUpdateDto build() {
        ClassroomCreateUpdateDto dto = new ClassroomCreateUpdateDto();
        dto.setName(name);
        return dto;
    }
}