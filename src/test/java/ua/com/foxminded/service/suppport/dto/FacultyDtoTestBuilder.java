package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class FacultyDtoTestBuilder {
    private String title = TestDataFactory.uniqueName("Faculty");
    private String code = TestDataFactory.uniqueName("FC");

    public static FacultyDtoTestBuilder aFaculty() {
        return new FacultyDtoTestBuilder();
    }

    public FacultyDtoTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public FacultyDtoTestBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public FacultyCreateUpdateDto build() {
        FacultyCreateUpdateDto dto = new FacultyCreateUpdateDto();
        dto.setCode(code);
        dto.setTitle(title);
        return dto;
    }
}
