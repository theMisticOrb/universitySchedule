package ua.com.foxminded.dto.user;

import ua.com.foxminded.enums.UserRole;

public class UserResponseDto {

    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    protected UserRole userRole;

    public UserResponseDto() {
    }

    public UserResponseDto(Integer id, String firstName, String middleName, String lastName, UserRole userRole) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.userRole = userRole;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

}
