package ua.com.foxminded.dto.classroom;

import java.util.ArrayList;
import java.util.List;

public class ClassroomCreateUpdateDtoList {

    private List<ClassroomCreateUpdateDto> list = new ArrayList<>();

    public ClassroomCreateUpdateDtoList() {
    }

    public ClassroomCreateUpdateDtoList(List<ClassroomCreateUpdateDto> list) {
        this.list = list;
    }

    public List<ClassroomCreateUpdateDto> getList() {
        return list;
    }

    public void setList(List<ClassroomCreateUpdateDto> list) {
        this.list = list;
    }

}
