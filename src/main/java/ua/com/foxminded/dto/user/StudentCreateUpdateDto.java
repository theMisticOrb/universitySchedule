package ua.com.foxminded.dto.user;

public class StudentCreateUpdateDto extends UserCreateUpdateDto {

    private Integer groupId;
    private String groupName;

    public StudentCreateUpdateDto() {
    }

    public StudentCreateUpdateDto(String firstName, String middleName, String lastName, Integer groupId,
            String groupName) {
        super(firstName, middleName, lastName);
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
