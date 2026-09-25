package ua.com.foxminded.service.suppport.dto;

import java.util.Collections;
import java.util.Set;

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.service.suppport.TestDataFactory;

public class StudentDtoTestBuilder {
    
    private String firstName = TestDataFactory.uniqueName("FirstName");
    private String middleName = TestDataFactory.uniqueName("MiddleName");
    private String lastName = TestDataFactory.uniqueName("LastName");
    private String email = TestDataFactory.uniqueName("student") + "@example.com";
    private String password = "Password123!";
    private Set<UserRole> roles = Collections.singleton(UserRole.STUDENT);
    private String groupName;   

    public static StudentDtoTestBuilder aStudent() {
        return new StudentDtoTestBuilder();
    }

    public StudentDtoTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public StudentDtoTestBuilder withMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public StudentDtoTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public StudentDtoTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public StudentDtoTestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public StudentDtoTestBuilder withRoles(Set<UserRole> roles) {
        this.roles = roles;
        return this;
    }

    public StudentDtoTestBuilder withGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public StudentCreateUpdateDto build() {
        if (groupName == null) {
            throw new IllegalStateException(
                "groupName is required — call withGroupName(...) before build()");
        }
        StudentCreateUpdateDto dto = new StudentCreateUpdateDto();
        dto.setFirstName(firstName);
        dto.setMiddleName(middleName);
        dto.setLastName(lastName);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRoles(roles);
        dto.setGroupName(groupName);   
        return dto;
    }

}
