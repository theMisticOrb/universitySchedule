package ua.com.foxminded.dto.subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectCreateUpdateListDto {

    private List<SubjectCreateUpdateDto> list = new ArrayList<>();

    public SubjectCreateUpdateListDto() {
    }

    public SubjectCreateUpdateListDto(List<SubjectCreateUpdateDto> list) {
        this.list = list;
    }

    public List<SubjectCreateUpdateDto> getList() {
        return list;
    }

    public void setList(List<SubjectCreateUpdateDto> list) {
        this.list = list;
    }

}
