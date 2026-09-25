package ua.com.foxminded.dto.user;

import java.util.ArrayList;
import java.util.List;

public class UserCreateUpdateListDto {

    private List<UserCreateUpdateDto> users = new ArrayList<>();

    public UserCreateUpdateListDto() {
    }

    public List<UserCreateUpdateDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserCreateUpdateDto> users) {
        this.users = users;
    }


}
