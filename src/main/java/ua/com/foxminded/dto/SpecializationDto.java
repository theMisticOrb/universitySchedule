package ua.com.foxminded.dto;

import java.util.List;

public class SpecializationDto {

    private Integer id;
    private String title;
    private Integer departmentId;
    private List<GroupDto> groups;

    public SpecializationDto() {
    }

    public SpecializationDto(Integer id, String title, Integer departmentId, List<GroupDto> groups) {
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

    public List<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDto> groups) {
        this.groups = groups;
    }

}
