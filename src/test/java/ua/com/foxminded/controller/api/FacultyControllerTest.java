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

import ua.com.foxminded.dto.faculty.FacultyCreateUpdateDto;
import ua.com.foxminded.dto.faculty.FacultyResponseDto;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.mapper.FacultyMapper;
import ua.com.foxminded.service.FacultyService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FacultyRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class FacultyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FacultyService service;
    @MockBean
    private FacultyMapper facultyMapper;

    @Autowired
    private ObjectMapper mapper;

    private Faculty createFaculty() {
        Faculty department = new Faculty();
        department.setId(1);
        department.setTitle("IT");
        return department;
    }

    private FacultyResponseDto createResponseDto() {
        FacultyResponseDto dto = new FacultyResponseDto();
        dto.setId(1);
        dto.setTitle("IT");
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();
        FacultyResponseDto responseDto = createResponseDto();

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);

        when(service.findAll()).thenReturn(faculties);
        when(facultyMapper.toResponseDto(any(Faculty.class))).thenReturn(responseDto);

        mvc.perform(get("/api/v1/faculties")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("IT"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();
        FacultyResponseDto responseDto = createResponseDto();

        when(service.findByTitle("IT")).thenReturn(faculty);
        when(facultyMapper.toResponseDto(faculty)).thenReturn(responseDto);

        mvc.perform(get("/api/v1/faculties/title/IT")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("IT"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByTitle("Math"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/v1/faculties/title/Math"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();
        FacultyResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(faculty);
        when(facultyMapper.toResponseDto(faculty)).thenReturn(responseDto);

        mvc.perform(get("/api/v1/faculties/1")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value("IT"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Faculty faculty = createFaculty();
        FacultyResponseDto responseDto = createResponseDto();

        FacultyCreateUpdateDto requestDto = new FacultyCreateUpdateDto();
        requestDto.setTitle("IT");

        when(service.addOne(any(FacultyCreateUpdateDto.class))).thenReturn(faculty);
        when(facultyMapper.toResponseDto(faculty)).thenReturn(responseDto);

        mvc.perform(post("/api/v1/faculties").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("IT"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createFaculty());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/v1/faculties/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();
        FacultyResponseDto responseDto = createResponseDto();

        FacultyCreateUpdateDto requestDto = new FacultyCreateUpdateDto();
        requestDto.setTitle("IT");

        when(service.fullUpdate(any(FacultyCreateUpdateDto.class), eq(1))).thenReturn(faculty);
        when(facultyMapper.toResponseDto(faculty)).thenReturn(responseDto);

        mvc.perform(put("/api/v1/faculties/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("IT"));

    }

}
