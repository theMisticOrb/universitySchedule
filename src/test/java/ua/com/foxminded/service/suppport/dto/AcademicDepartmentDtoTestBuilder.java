package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class AcademicDepartmentDtoTestBuilder {

    private String title = TestDataFactory.uniqueName("Department");
    private String code = TestDataFactory.uniqueName("DP");
    private String facultyCode;

    public static AcademicDepartmentDtoTestBuilder anAcademicDepartment() {
        return new AcademicDepartmentDtoTestBuilder();
    }

    public AcademicDepartmentDtoTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public AcademicDepartmentDtoTestBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    public AcademicDepartmentDtoTestBuilder withFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
        return this;
    }

    public AcademicDepartmentCreateUpdateDto build() {
        if (facultyCode == null) {
            throw new IllegalStateException("facultyCode is required — call withFacultyCode(...) before build()");
        }
        AcademicDepartmentCreateUpdateDto dto = new AcademicDepartmentCreateUpdateDto();
        dto.setCode(code);
        dto.setFacultyCode(facultyCode);
        dto.setTitle(title);
        return dto;
    }

}
