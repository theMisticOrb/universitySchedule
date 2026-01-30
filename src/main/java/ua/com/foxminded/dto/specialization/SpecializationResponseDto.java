package ua.com.foxminded.dto.specialization;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;

public class SpecializationResponseDto {

    private Integer id;
    private String title;
    private Integer departmentId;
    private List<GroupCreateUpdateDto> groups;

    public SpecializationResponseDto() {
    }

    public SpecializationResponseDto(Integer id, String title, Integer departmentId,
            List<GroupCreateUpdateDto> groups) {
        this.id = id;
        this.title = title;
        this.departmentId = departmentId;
        this.groups = groups;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
