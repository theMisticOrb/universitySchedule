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

import ua.com.foxminded.dto.department.AcademicDepartmentCreateUpdateDto;
import ua.com.foxminded.dto.department.AcademicDepartmentResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.service.AcademicDepartmentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AcademicDepartmentRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class AcademicDepartmentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AcademicDepartmentService service;

    @MockBean
    private AcademicDepartmentMapper departmentMapper;

    @Autowired
    private ObjectMapper mapper;

    private AcademicDepartment createDepartment() {
        AcademicDepartment department = new AcademicDepartment();
        department.setId(1);
        department.setFaculty(new Faculty(1, "IT", null, null));
        department.setTitle("Computer Science");
        return department;
    }

    private AcademicDepartmentResponseDto createResponseDto() {
        AcademicDepartmentResponseDto dto = new AcademicDepartmentResponseDto();
        dto.setId(1);
        dto.setFacultyCode("CS");
        dto.setTitle("Computer Science");
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        AcademicDepartment department = createDepartment();
        AcademicDepartmentResponseDto dto = createResponseDto();

        List<AcademicDepartment> departments = new ArrayList<>();
        departments.add(department);

        when(service.findAll()).thenReturn(departments);
        when(departmentMapper.toResponseDto(any(AcademicDepartment.class))).thenReturn(dto);

        mvc.perform(get("/api/v1/departments")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Computer Science"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        AcademicDepartment department = createDepartment();
        AcademicDepartmentResponseDto dto = createResponseDto();

        when(service.findByTitle("Computer Science")).thenReturn(department);
        when(departmentMapper.toResponseDto(department)).thenReturn(dto);
        mvc.perform(get("/api/v1/departments/title/Computer Science")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByTitle("Math"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/v1/departments/title/Math"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        AcademicDepartment department = createDepartment();
        AcademicDepartmentResponseDto dto = createResponseDto();

        when(service.findById(1)).thenReturn(department);
        when(departmentMapper.toResponseDto(department)).thenReturn(dto);
        mvc.perform(get("/api/v1/departments/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        AcademicDepartment department = createDepartment();
        AcademicDepartmentResponseDto responseDto = createResponseDto();

        AcademicDepartmentCreateUpdateDto requestDto = new AcademicDepartmentCreateUpdateDto();
        requestDto.setFacultyCode("CS");
        requestDto.setTitle("Computer Science");

        when(service.addOne(any(AcademicDepartmentCreateUpdateDto.class))).thenReturn(department);
        when(departmentMapper.toResponseDto(department)).thenReturn(responseDto);

        mvc.perform(post("/api/v1/departments").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createDepartment());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/v1/departments/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        AcademicDepartment department = createDepartment();

        AcademicDepartmentResponseDto responseDto = createResponseDto();

        AcademicDepartmentCreateUpdateDto requestDto = new AcademicDepartmentCreateUpdateDto();
        requestDto.setFacultyCode("CS");
        requestDto.setTitle("Computer Science");

        when(service.fullUpdate(any(AcademicDepartmentCreateUpdateDto.class), eq(1))).thenReturn(department);
        when(departmentMapper.toResponseDto(department)).thenReturn(responseDto);

        mvc.perform(put("/api/v1/departments/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));

        verify(service).fullUpdate(any(AcademicDepartmentCreateUpdateDto.class), eq(1));
        verify(departmentMapper).toResponseDto(department);

    }
}
