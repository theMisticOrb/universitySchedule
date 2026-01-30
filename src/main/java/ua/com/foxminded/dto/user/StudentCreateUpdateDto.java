package ua.com.foxminded.dto.user;

public class StudentCreateUpdateDto extends UserCreateUpdateDto {

    private Integer groupId;

    public StudentCreateUpdateDto() {
    }

    public StudentCreateUpdateDto(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

}
