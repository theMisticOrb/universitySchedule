package ua.com.foxminded.dto.specialization;

import java.util.List;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;

public class SpecializationResponseDto {

    private Integer id;
    private String title;
    private String code;
    private String departmentCode;
    private List<GroupCreateUpdateDto> groups;

    public SpecializationResponseDto() {
    }

    public SpecializationResponseDto(Integer id, String title, String code, String departmentCode,
            List<GroupCreateUpdateDto> groups) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.departmentCode = departmentCode;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String string) {
        this.departmentCode = string;
    }

    public List<GroupCreateUpdateDto> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupCreateUpdateDto> groups) {
        this.groups = groups;
    }

}
