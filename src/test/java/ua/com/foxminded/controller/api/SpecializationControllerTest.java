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
import ua.com.foxminded.dto.specialization.SpecializationCreateUpdateDto;
import ua.com.foxminded.dto.specialization.SpecializationResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.service.SpecializationService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SpecializationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class SpecializationControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SpecializationService service;

    @MockBean
    private SpecializationMapper specializationMapper;

    @Autowired
    private ObjectMapper mapper;

    private Specialization createSpecialization() {
        Specialization entity = new Specialization();
        entity.setId(1);
        entity.setDepartment(new AcademicDepartment(1, "IT", null, null));
        entity.setTitle("Computer Science");
        return entity;
    }

    private SpecializationResponseDto createResponseDto() {
        SpecializationResponseDto dto = new SpecializationResponseDto();
        dto.setId(1);
        dto.setDepartmentId(1);
        dto.setTitle("Computer Science");
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Specialization specialization = createSpecialization();
        SpecializationResponseDto responseDto = createResponseDto();

        List<Specialization> specializations = new ArrayList<>();
        specializations.add(specialization);

        when(service.findAll()).thenReturn(specializations);
        when(specializationMapper.toResponseDto(any(Specialization.class))).thenReturn(responseDto);

        mvc.perform(get("/api/specializations")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Computer Science"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Specialization specialization = createSpecialization();
        SpecializationResponseDto dto = createResponseDto();

        when(service.findByTitle("Computer Science")).thenReturn(specialization);
        when(specializationMapper.toResponseDto(specialization)).thenReturn(dto);
        mvc.perform(get("/api/specializations/title/Computer Science")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByTitle("Math"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/specializations/title/Math"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Specialization specialization = createSpecialization();
        SpecializationResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(specialization);
        when(specializationMapper.toResponseDto(specialization)).thenReturn(responseDto);
        mvc.perform(get("/api/specializations/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Specialization specialization = createSpecialization();
        SpecializationResponseDto responseDto = createResponseDto();
        
        SpecializationCreateUpdateDto requestDto = new SpecializationCreateUpdateDto();
        requestDto.setDepartmentId(1);
        requestDto.setTitle("Computer Science");

        when(service.addOne(any(SpecializationCreateUpdateDto.class))).thenReturn(specialization);
        when(specializationMapper.toResponseDto(specialization)).thenReturn(responseDto);

        mvc.perform(post("/api/specializations").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createSpecialization());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/specializations/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Specialization specialization = createSpecialization();
        SpecializationResponseDto responseDto = createResponseDto();

        AcademicDepartmentCreateUpdateDto requestDto = new AcademicDepartmentCreateUpdateDto();
        requestDto.setFacultyId(1);
        requestDto.setTitle("Computer Science");

        when(service.update(any(SpecializationCreateUpdateDto.class), eq(1))).thenReturn(specialization);
        when(specializationMapper.toResponseDto(specialization)).thenReturn(responseDto);

        mvc.perform(put("/api/specializations/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));

        verify(service).update(any(SpecializationCreateUpdateDto.class), eq(1));
        verify(specializationMapper).toResponseDto(specialization);

    }

}
