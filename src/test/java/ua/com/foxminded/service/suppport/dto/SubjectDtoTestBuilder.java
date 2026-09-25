package ua.com.foxminded.service.suppport.dto;

import java.util.ArrayList;
import java.util.List;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class SubjectDtoTestBuilder {

    private String title = TestDataFactory.uniqueName("Subject");
    private String departmentCode;
    private List<Integer> teacherIds = new ArrayList<>();

    public static SubjectDtoTestBuilder aSubject() {
        return new SubjectDtoTestBuilder();
    }

    public SubjectDtoTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public SubjectDtoTestBuilder withDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
        return this;
    }

    public SubjectDtoTestBuilder withTeacherIds(List<Integer> teacherIds) {
        this.teacherIds = teacherIds;
        return this;
    }

    public SubjectCreateUpdateDto build() {
        if (departmentCode == null) {
            throw new IllegalStateException("departmentCode is required — call withDepartmentCode(...) before build()");
        }
        SubjectCreateUpdateDto dto = new SubjectCreateUpdateDto();
        dto.setTitle(title);
        dto.setDepartmentCode(departmentCode);
        dto.setTeacherIds(teacherIds);
        return dto;
    }
}