package ua.com.foxminded.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.dto.group.GroupCreateUpdateDto;
import ua.com.foxminded.dto.group.GroupResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.mapper.GroupMapper;
import ua.com.foxminded.service.GroupService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupService service;
    @MockBean
    private GroupMapper groupMapper;
    
    @Autowired
    private ObjectMapper mapper;

    private Group createGroup() {
        Group group = new Group();
        group.setId(1);
        group.setName("1.1.1.1");
        return group;
    }
    
    private GroupResponseDto createResponseDto() {
        GroupResponseDto dto = new GroupResponseDto();
        dto.setId(1);
        dto.setName("1.1.1.1");
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Group group = createGroup();
        GroupResponseDto responseDto = createResponseDto();
        
        List<Group> groups = new ArrayList<>();
        groups.add(group);

        when(service.findAll()).thenReturn(groups);
        when(groupMapper.toDto(any(Group.class))).thenReturn(responseDto);

        mvc.perform(get("/api/groups")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value(group.getName()));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Group group = createGroup();
        GroupResponseDto responseDto = createResponseDto();

        when(service.findByName("1.1.1.1")).thenReturn(group);
        when(groupMapper.toDto(group)).thenReturn(responseDto);

        mvc.perform(get("/api/groups/name/1.1.1.1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("1.1.1.1"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByName("Math"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/groups/name/Math"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Group group = createGroup();
        GroupResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(group);
        when(groupMapper.toDto(group)).thenReturn(responseDto);

        mvc.perform(get("/api/groups/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(group.getName()));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Group group = createGroup();
        GroupResponseDto responseDto = createResponseDto();
        GroupCreateUpdateDto requestDto = new GroupCreateUpdateDto();
        requestDto.setFacultyId(1);

        when(service.addOne(any(GroupCreateUpdateDto.class))).thenReturn(group);
        when(groupMapper.toDto(group)).thenReturn(responseDto);

        mvc.perform(post("/api/groups").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(group.getName()));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createGroup());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/groups/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Group group = createGroup();
        GroupResponseDto responseDto = createResponseDto();

        GroupCreateUpdateDto requestDto = new GroupCreateUpdateDto();
        requestDto.setFacultyId(1);

        when(service.update(any(GroupCreateUpdateDto.class), eq(1))).thenReturn(group);
        when(groupMapper.toDto(group)).thenReturn(responseDto);

        mvc.perform(put("/api/groups/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(group.getName()));

    }

}
