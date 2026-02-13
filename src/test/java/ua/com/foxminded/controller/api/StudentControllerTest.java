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

import ua.com.foxminded.dto.user.StudentCreateUpdateDto;
import ua.com.foxminded.dto.user.StudentResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.service.StudentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudentRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService service;

    @MockBean
    private StudentMapper studentMapper;

    @Autowired
    private ObjectMapper mapper;

    private Student createStudent() {
        Student entity = new Student();
        entity.setId(1);
        entity.setFirstName("Bob");
        entity.setMiddleName("Bobovitch");
        entity.setLastName("Bobinski");
        entity.setGroup(new Group(1, null, null, null, null, null, null));
        return entity;
    }

    private StudentResponseDto createResponseDto() {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(1);
        dto.setFirstName("Bob");
        dto.setMiddleName("Bobovitch");
        dto.setLastName("Bobinski");
        dto.setGroupId(1);
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Student entity = createStudent();
        StudentResponseDto responseDto = createResponseDto();

        List<Student> students = new ArrayList<>();
        students.add(entity);

        when(service.findAll()).thenReturn(students);
        when(studentMapper.toDto(any(Student.class))).thenReturn(responseDto);

        mvc.perform(get("/api/students")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Student student = createStudent();
        StudentResponseDto dto = createResponseDto();

        when(service.findByFullName("Bobinski", "Bob", "Bobovitch")).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(dto);

        mvc.perform(get("/api/students/search").param("lastName", "Bobinski").param("firstName", "Bob")
                .param("middleName", "Bobovitch")).andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Bobinski")).andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.middleName").value("Bobovitch"));

    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByFullName("Bobinski", "Devid", "Bobovitch"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/students/search")
                .param("lastName", "Bobinski")
                .param("firstName", "Devid")
                .param("middleName", "Bobovitch"))
        .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Student student = createStudent();
        StudentResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(responseDto);
        mvc.perform(get("/api/students/1")).andExpect(status().isOk());
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Student student = createStudent();
        StudentResponseDto responseDto = createResponseDto();

        StudentCreateUpdateDto requestDto = new StudentCreateUpdateDto();
        requestDto.setGroupId(1);
        requestDto.setFirstName("Bob");
        requestDto.setMiddleName("Bobovitch");
        requestDto.setLastName("Bobinski");

        when(service.addOne(any(StudentCreateUpdateDto.class))).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(responseDto);

        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated());
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createStudent());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/students/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Student student = createStudent();
        StudentResponseDto responseDto = createResponseDto();

        StudentCreateUpdateDto requestDto = new StudentCreateUpdateDto();
        requestDto.setGroupId(1);
        requestDto.setFirstName("Bob");
        requestDto.setMiddleName("Bobovitch");
        requestDto.setLastName("Bobinski");

        when(service.update(any(StudentCreateUpdateDto.class), eq(1))).thenReturn(student);
        when(studentMapper.toDto(student)).thenReturn(responseDto);

        mvc.perform(put("/api/students/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk());

        verify(service).update(any(StudentCreateUpdateDto.class), eq(1));
        verify(studentMapper).toDto(student);

    }

}
