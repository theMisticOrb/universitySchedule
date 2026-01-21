package ua.com.foxminded.dto;

public class StudentDto extends UserDto {

    private Integer groupId;

    public StudentDto() {
    }

    public StudentDto(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
