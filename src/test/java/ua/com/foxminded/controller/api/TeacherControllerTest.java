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

import ua.com.foxminded.dto.user.TeacherCreateUpdateDto;
import ua.com.foxminded.dto.user.TeacherResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.service.TeacherService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TeacherRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeacherControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeacherService service;

    @MockBean
    private TeacherMapper studentMapper;

    @Autowired
    private ObjectMapper mapper;

    private Teacher createTeacher() {
        Teacher entity = new Teacher();
        entity.setId(1);
        entity.setFirstName("Bob");
        entity.setMiddleName("Bobovitch");
        entity.setLastName("Bobinski");
        entity.setDepartment(new AcademicDepartment(1, null, null, null));
        return entity;
    }

    private TeacherResponseDto createResponseDto() {
        TeacherResponseDto dto = new TeacherResponseDto();
        dto.setId(1);
        dto.setFirstName("Bob");
        dto.setMiddleName("Bobovitch");
        dto.setLastName("Bobinski");
        dto.setDepartmentId(1);
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Teacher entity = createTeacher();
        TeacherResponseDto responseDto = createResponseDto();

        List<Teacher> students = new ArrayList<>();
        students.add(entity);

        when(service.findAll()).thenReturn(students);
        when(studentMapper.toResponseDto(any(Teacher.class))).thenReturn(responseDto);

        mvc.perform(get("/api/teachers")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Teacher student = createTeacher();
        TeacherResponseDto dto = createResponseDto();

        when(service.findByFullName("Bobinski", "Bob", "Bobovitch")).thenReturn(student);
        when(studentMapper.toResponseDto(student)).thenReturn(dto);

        mvc.perform(get("/api/teachers/search").param("lastName", "Bobinski").param("firstName", "Bob")
                .param("middleName", "Bobovitch")).andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Bobinski")).andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.middleName").value("Bobovitch"));

    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByFullName("Bobinski", "Devid", "Bobovitch"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/teachers/search")
                .param("lastName", "Bobinski")
                .param("firstName", "Devid")
                .param("middleName", "Bobovitch"))
        .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Teacher entity = createTeacher();
        TeacherResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(entity);
        when(studentMapper.toResponseDto(entity)).thenReturn(responseDto);
        mvc.perform(get("/api/teachers/1")).andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Teacher entity = createTeacher();
        TeacherResponseDto responseDto = createResponseDto();

        TeacherCreateUpdateDto requestDto = new TeacherCreateUpdateDto();
        requestDto.setDepartmentId(1);
        requestDto.setFirstName("Bob");
        requestDto.setMiddleName("Bobovitch");
        requestDto.setLastName("Bobinski");

        when(service.addOne(any(TeacherCreateUpdateDto.class))).thenReturn(entity);
        when(studentMapper.toResponseDto(entity)).thenReturn(responseDto);

        mvc.perform(post("/api/teachers").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated());
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createTeacher());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/teachers/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Teacher entity = createTeacher();
        TeacherResponseDto responseDto = createResponseDto();

        TeacherCreateUpdateDto requestDto = new TeacherCreateUpdateDto();
        requestDto.setDepartmentId(1);
        requestDto.setFirstName("Bob");
        requestDto.setMiddleName("Bobovitch");
        requestDto.setLastName("Bobinski");

        when(service.update(any(TeacherCreateUpdateDto.class), eq(1))).thenReturn(entity);
        when(studentMapper.toResponseDto(entity)).thenReturn(responseDto);

        mvc.perform(put("/api/teachers/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk());

        verify(service).update(any(TeacherCreateUpdateDto.class), eq(1));
        verify(studentMapper).toResponseDto(entity);

    }

}
