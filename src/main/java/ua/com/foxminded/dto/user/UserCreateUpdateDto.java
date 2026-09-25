package ua.com.foxminded.dto.user;

import java.util.Set;

import ua.com.foxminded.enums.UserRole;

public class UserCreateUpdateDto {

    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
    private Set<UserRole> roles;
    private String groupName; //only for students
    private String departmentCode;//only for teachers

    public UserCreateUpdateDto() {
    }

    public UserCreateUpdateDto(String firstName, String middleName, String lastName, String email, String password,
            Set<UserRole> roles, String groupName, String departmentCode) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.groupName = groupName;
        this.departmentCode = departmentCode;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
