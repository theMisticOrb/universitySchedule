package ua.com.foxminded.service.suppport.dto;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;

public class GroupDtoTestBuilder {

    private String specializationCode;

    public static GroupDtoTestBuilder aGroup() {
        return new GroupDtoTestBuilder();
    }

    public GroupDtoTestBuilder withSpecializationCode(String specializationCode) {
        this.specializationCode = specializationCode;
        return this;
    }

    public GroupCreateUpdateDto build() {
        if (specializationCode == null) {
            throw new IllegalStateException(
                    "specializationCode is required — call withSpecializationCode(...) before build()");
        }
        GroupCreateUpdateDto dto = new GroupCreateUpdateDto();
        dto.setSpecializationCode(specializationCode);
        return dto;
    }
}