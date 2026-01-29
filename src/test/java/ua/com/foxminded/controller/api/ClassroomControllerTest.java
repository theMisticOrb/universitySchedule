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

import ua.com.foxminded.dto.ClassroomDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.service.ClassroomService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ClassroomController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClassroomControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClassroomService service;

    @Autowired
    private ObjectMapper mapper;

    private Classroom createClassroom() {
        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setName("2A");
        return classroom;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();

        List<Classroom> listClassrooms = new ArrayList<>();
        listClassrooms.add(classroom);

        when(service.findAll()).thenReturn(listClassrooms);

        mvc.perform(get("/api/classrooms")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("2A"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();

        when(service.findByName("2A")).thenReturn(classroom);

        mvc.perform(get("/api/classrooms/name/2A")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2A"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByName("2B"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/classrooms/name/2B"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = createClassroom();

        when(service.findById(1)).thenReturn(classroom);

        mvc.perform(get("/api/classrooms/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("2A"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Classroom classroom = createClassroom();
        ClassroomDto classroomDto = new ClassroomDto();
        classroomDto.setName("2A");

        when(service.addOne(any(ClassroomDto.class))).thenReturn(classroom);

        mvc.perform(post("/api/classrooms").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(classroomDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("2A"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/classrooms/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Classroom classroom = new Classroom();
        classroom.setName("3B");

        ClassroomDto classroomDto = new ClassroomDto();
        classroomDto.setName("3B");

        when(service.update(any(ClassroomDto.class), eq(1))).thenReturn(classroom);

        mvc.perform(put("/api/classrooms/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(classroomDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("3B"));

    }
}
