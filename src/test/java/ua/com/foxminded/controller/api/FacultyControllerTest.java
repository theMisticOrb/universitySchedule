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
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.service.FacultyService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FacultyController.class)
@AutoConfigureMockMvc(addFilters = false)
class FacultyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FacultyService service;

    @Autowired
    private ObjectMapper mapper;

    private Faculty createFaculty() {
        Faculty department = new Faculty();
        department.setId(1);
        department.setTitle("IT");
        return department;
    }
    
    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);

        when(service.findAll()).thenReturn(faculties);

        mvc.perform(get("/api/faculties")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("IT"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();

        when(service.findByTitle("IT")).thenReturn(faculty);

        mvc.perform(get("/api/faculties/title/IT")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("IT"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByTitle("Math"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/faculties/title/Math"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = createFaculty();

        when(service.findById(1)).thenReturn(faculty);

        mvc.perform(get("/api/faculties/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("IT"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Faculty faculty = createFaculty();
        FacultyCreateUpdateDto facultyDto = new FacultyCreateUpdateDto();
        facultyDto.setTitle("IT");

        when(service.addOne(any(FacultyCreateUpdateDto.class))).thenReturn(faculty);

        mvc.perform(post("/api/faculties").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(facultyDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("IT"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/faculties/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setTitle("IT");

        FacultyCreateUpdateDto departmentDto = new FacultyCreateUpdateDto();
        departmentDto.setTitle("IT");

        when(service.update(any(FacultyCreateUpdateDto.class), eq(1))).thenReturn(faculty);

        mvc.perform(put("/api/faculties/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(departmentDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("IT"));

    }

}
