package ua.com.foxminded.controller.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.com.foxminded.dto.group.GroupResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.service.GroupService;

@WebMvcTest(GroupViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class GroupViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService service;

    @MockBean
    private GroupMapper mapper;

    @Test
    void listGroups_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Group> groups = Arrays.asList(new Group());
        GroupResponseDto dto = new GroupResponseDto();

        when(service.findAll()).thenReturn(groups);
        when(mapper.toResponseDto(any(Group.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/groups")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("group/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("groups"));

        verify(service, times(1)).findAll();

    }
}
