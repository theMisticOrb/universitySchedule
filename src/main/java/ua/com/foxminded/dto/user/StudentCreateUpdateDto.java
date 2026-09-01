package ua.com.foxminded.dto.user;

public class StudentCreateUpdateDto extends UserCreateUpdateDto {

    private String groupName;

    public StudentCreateUpdateDto() {
    }

    public StudentCreateUpdateDto( String groupName) {
        this.groupName = groupName;
    }


    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
