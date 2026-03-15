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

import ua.com.foxminded.dto.classroom.ClassroomCreateUpdateDto;
import ua.com.foxminded.dto.classroom.ClassroomResponseDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.mapper.ClassroomMapper;
import ua.com.foxminded.service.ClassroomService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClassroomRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClassroomControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClassroomService service;

    @MockBean
    private ClassroomMapper classroomMapper;

    @Autowired
    private ObjectMapper mapper;

    private Classroom createClassroom() {
        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setName("2A");
        return classroom;
    }

    private ClassroomResponseDto createResponseDto() {
        ClassroomResponseDto dto = new ClassroomResponseDto();
        dto.setId(1);
        dto.setName("2A");
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();
        ClassroomResponseDto responseDto = createResponseDto();

        List<Classroom> listClassrooms = new ArrayList<>();
        listClassrooms.add(classroom);

        when(service.findAll()).thenReturn(listClassrooms);
        when(classroomMapper.toResponseDto(any(Classroom.class))).thenReturn(responseDto);

        mvc.perform(get("/api/v1/classrooms")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("2A"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();
        ClassroomResponseDto responseDto = createResponseDto();

        when(service.findByName("2A")).thenReturn(classroom);
        when(classroomMapper.toResponseDto(classroom)).thenReturn(responseDto);

        mvc.perform(get("/api/v1/classrooms/name/2A")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2A"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByName("2B"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/v1/classrooms/name/2B"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();
        ClassroomResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(classroom);
        when(classroomMapper.toResponseDto(classroom)).thenReturn(responseDto);

        mvc.perform(get("/api/v1/classrooms/1")).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("2A"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Classroom classroom = createClassroom();
        ClassroomResponseDto responseDto = createResponseDto();

        ClassroomCreateUpdateDto requestDto = new ClassroomCreateUpdateDto();
        requestDto.setName("2A");

        when(service.addOne(any(ClassroomCreateUpdateDto.class))).thenReturn(classroom);
        when(classroomMapper.toResponseDto(classroom)).thenReturn(responseDto);

        mvc.perform(post("/api/v1/classrooms").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("2A"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createClassroom());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/v1/classrooms/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();
        ClassroomResponseDto responseDto = createResponseDto();

        ClassroomCreateUpdateDto requestDto = new ClassroomCreateUpdateDto();
        requestDto.setName("2A");

        when(service.update(any(ClassroomCreateUpdateDto.class), eq(1))).thenReturn(classroom);
        when(classroomMapper.toResponseDto(classroom)).thenReturn(responseDto);

        mvc.perform(put("/api/v1/classrooms/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2A"));

    }
}
