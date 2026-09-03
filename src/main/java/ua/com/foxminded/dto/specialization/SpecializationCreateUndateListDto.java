package ua.com.foxminded.dto.specialization;

import java.util.ArrayList;
import java.util.List;

public class SpecializationCreateUndateListDto {

    private List<SpecializationCreateUpdateDto> list = new ArrayList<>();

    public SpecializationCreateUndateListDto() {
    }

    public SpecializationCreateUndateListDto(List<SpecializationCreateUpdateDto> list) {
        this.list = list;
    }

    public List<SpecializationCreateUpdateDto> getList() {
        return list;
    }

    public void setList(List<SpecializationCreateUpdateDto> list) {
        this.list = list;
    }

}
