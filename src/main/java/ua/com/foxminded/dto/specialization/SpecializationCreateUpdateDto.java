package ua.com.foxminded.dto.specialization;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;

public class SpecializationCreateUpdateDto {

    private String title;
    private String code;
    private Integer departmentId;
    private List<GroupCreateUpdateDto> groups;

    public SpecializationCreateUpdateDto() {
    }

    public SpecializationCreateUpdateDto(String title, String code, Integer departmentId,
            List<GroupCreateUpdateDto> groups) {
        this.title = title;
        this.code = code;
        this.departmentId = departmentId;
        this.groups = groups;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public List<GroupCreateUpdateDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupCreateUpdateDto> groups) {
        this.groups = groups;
    }

}
