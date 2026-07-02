package ua.com.foxminded.dto.user;

import java.util.ArrayList;
import java.util.List;

public class UserCreateUpdateListDto {

    private List<UserCreateUpdateDto> userList = new ArrayList<>();

    public UserCreateUpdateListDto() {
    }

    public UserCreateUpdateListDto(List<UserCreateUpdateDto> userList) {
        this.userList = userList;
    }

    public List<UserCreateUpdateDto> getUserList() {
        return userList;
    }

    public void setUserList(List<UserCreateUpdateDto> userList) {
        this.userList = userList;
    }

}
