package ua.com.foxminded.dto.user;

public class StudentResponseDto extends UserResponseDto {

    private String groupName;

    public StudentResponseDto() {
    }

    public StudentResponseDto( String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
