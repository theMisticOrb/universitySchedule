package ua.com.foxminded.service.suppport.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class TeacherDtoTestBuilder {

    private String firstName = TestDataFactory.uniqueName("FirstName");
    private String middleName = TestDataFactory.uniqueName("MiddleName");
    private String lastName = TestDataFactory.uniqueName("LastName");
    private String email = TestDataFactory.uniqueName("teacher") + "@example.com";
    private String password = "Password123!";
    private Set<UserRole> roles = Collections.singleton(UserRole.TEACHER);
    private String departmentCode; 
    private List<SubjectCreateUpdateDto> subjects = new ArrayList<>();

    public static TeacherDtoTestBuilder aTeacher() {
        return new TeacherDtoTestBuilder();
    }

    public TeacherDtoTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TeacherDtoTestBuilder withMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public TeacherDtoTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TeacherDtoTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public TeacherDtoTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public TeacherDtoTestBuilder withRoles(Set<UserRole> roles) {
        this.roles = roles;
        return this;
    }

    public TeacherDtoTestBuilder withDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
        return this;
    }

    public TeacherDtoTestBuilder withSubjectTitles(List<String> subjectTitles) {
        this.subjects = subjectTitles.stream().map(title -> {
            SubjectCreateUpdateDto subjectDto = new SubjectCreateUpdateDto();
            subjectDto.setTitle(title);
            return subjectDto;
        }).collect(Collectors.toList());
        return this;
    }

    public TeacherCreateUpdateDto build() {
        if (departmentCode == null) {
            throw new IllegalStateException("departmentCode is required — call withDepartmentCode(...) before build()");
        }
        TeacherCreateUpdateDto dto = new TeacherCreateUpdateDto();
        dto.setFirstName(firstName);
        dto.setMiddleName(middleName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRoles(roles);
        dto.setDepartmentCode(departmentCode);
        dto.setSubjects(subjects);
        return dto;
    }
}
