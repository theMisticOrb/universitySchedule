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

    public UserCreateUpdateDto() {
    }

    public UserCreateUpdateDto(String firstName, String middleName, String lastName, String email, String password,
            Set<UserRole> roles) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
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

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> userRoles) {
        this.roles = userRoles;
    }

}
