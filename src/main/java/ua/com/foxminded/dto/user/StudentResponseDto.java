package ua.com.foxminded.dto.user;

public class StudentResponseDto extends UserResponseDto {

    private Integer groupId;
    private String groupName;

    public StudentResponseDto() {
    }

    public StudentResponseDto(Integer id, String firstName, String middleName, String lastName, Integer groupId,
            String groupName) {
        super(id, firstName, middleName, lastName);
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
