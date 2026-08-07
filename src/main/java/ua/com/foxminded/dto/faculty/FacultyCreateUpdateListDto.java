package ua.com.foxminded.dto.faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyCreateUpdateListDto {

    private List<FacultyCreateUpdateDto> list = new ArrayList<>();

    public FacultyCreateUpdateListDto(List<FacultyCreateUpdateDto> list) {
        this.list = list;
    }

    public List<FacultyCreateUpdateDto> getList() {
        return list;
    }

    public void setList(List<FacultyCreateUpdateDto> list) {
        this.list = list;
    }
    
    
    
}
