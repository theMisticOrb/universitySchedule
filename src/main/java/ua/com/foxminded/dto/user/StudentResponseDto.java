package ua.com.foxminded.dto.user;

public class StudentResponseDto extends UserResponseDto {

    private Integer groupId;

    public StudentResponseDto() {
    }

    public StudentResponseDto(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
