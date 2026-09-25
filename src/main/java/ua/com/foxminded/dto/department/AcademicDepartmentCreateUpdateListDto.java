package ua.com.foxminded.dto.department;

import java.util.ArrayList;
import java.util.List;

public class AcademicDepartmentCreateUpdateListDto {

    private List<AcademicDepartmentCreateUpdateDto> list = new ArrayList<>();

    public AcademicDepartmentCreateUpdateListDto() {
    }

    public AcademicDepartmentCreateUpdateListDto(List<AcademicDepartmentCreateUpdateDto> list) {
        this.list = list;
    }

    public List<AcademicDepartmentCreateUpdateDto> getList() {
        return list;
    }

    public void setList(List<AcademicDepartmentCreateUpdateDto> list) {
        this.list = list;
    }

}
