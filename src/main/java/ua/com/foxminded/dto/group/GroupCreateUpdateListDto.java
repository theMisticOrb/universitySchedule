package ua.com.foxminded.dto.group;

import java.util.ArrayList;
import java.util.List;

public class GroupCreateUpdateListDto {

    private List<GroupCreateUpdateDto> list = new ArrayList<>();

    public GroupCreateUpdateListDto() {
    }

    public GroupCreateUpdateListDto(List<GroupCreateUpdateDto> list) {
        this.list = list;
    }

    public List<GroupCreateUpdateDto> getList() {
        return list;
    }

    public void setList(List<GroupCreateUpdateDto> list) {
        this.list = list;
    }

}
