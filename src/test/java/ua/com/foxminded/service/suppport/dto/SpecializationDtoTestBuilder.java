package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class SpecializationDtoTestBuilder {

    private String title = TestDataFactory.uniqueName("Specialization");
    private String code = TestDataFactory.uniqueName("SP");
    private String departmentCode;

    public static SpecializationDtoTestBuilder aSpecialization() {
        return new SpecializationDtoTestBuilder();
    }

    public SpecializationDtoTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public SpecializationDtoTestBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public SpecializationDtoTestBuilder withDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
        return this;
    }

    public SpecializationCreateUpdateDto build() {
        if (departmentCode == null) {
            throw new IllegalStateException("departmentCode is required — call withDepartmentCode(...) before build()");
        }
        SpecializationCreateUpdateDto dto = new SpecializationCreateUpdateDto();
        dto.setTitle(title);
        dto.setCode(code);
        dto.setDepartmentCode(departmentCode);
        return dto;
    }
}