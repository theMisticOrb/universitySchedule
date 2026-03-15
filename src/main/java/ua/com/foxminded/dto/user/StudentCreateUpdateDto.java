package ua.com.foxminded.dto.user;

public class StudentCreateUpdateDto extends UserCreateUpdateDto {

    private Integer groupId;
    private String groupName;

    public StudentCreateUpdateDto() {
    }

    public StudentCreateUpdateDto(Integer groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
