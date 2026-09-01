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

import ua.com.foxminded.dto.subject.SubjectCreateUpdateDto;
import ua.com.foxminded.dto.subject.SubjectResponseDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.service.SubjectService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SubjectRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class SubjectControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SubjectService service;

    @MockBean
    private SubjectMapper specializationMapper;

    @Autowired
    private ObjectMapper mapper;

    private Subject createSubject() {
        Subject entity = new Subject();
        Teacher teacher = new Teacher();
        teacher.setId(1);

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        entity.setId(1);
        entity.setTeachers(teachers);
        entity.setTitle("Computer Science");
        return entity;
    }

    private SubjectResponseDto createResponseDto() {
        SubjectResponseDto dto = new SubjectResponseDto();

        List<String> teacherEmails = new ArrayList<>();
        teacherEmails.add("teacher@gmail.com");

        dto.setId(1);
        dto.setTeacherEmails(teacherEmails);
        dto.setTitle("Computer Science");
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Subject subject = createSubject();
        SubjectResponseDto responseDto = createResponseDto();

        List<Subject> specializations = new ArrayList<>();
        specializations.add(subject);

        when(service.findAll()).thenReturn(specializations);
        when(specializationMapper.toResponseDto(any(Subject.class))).thenReturn(responseDto);

        mvc.perform(get("/api/v1/subjects")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Computer Science"));
    }

    @Test
    void findByTitle_shouldReturn200_whenCalled() throws Exception {

        Subject subject = createSubject();
        SubjectResponseDto responseDto = createResponseDto();

        when(service.findByTitle("Computer Science")).thenReturn(subject);
        when(specializationMapper.toResponseDto(subject)).thenReturn(responseDto);
        mvc.perform(get("/api/v1/subjects/title/Computer Science")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void findByTitle_shouldReturn404_whenNotFound() throws Exception {
        when(service.findByTitle("Math"))
            .thenThrow(new EntityNotFoundException("Not found"));

        mvc.perform(get("/api/v1/subjects/title/Math"))
           .andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Subject subject = createSubject();
        SubjectResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(subject);
        when(specializationMapper.toResponseDto(subject)).thenReturn(responseDto);
        mvc.perform(get("/api/v1/subjects/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Subject subject = createSubject();
        SubjectResponseDto responseDto = createResponseDto();

        SubjectCreateUpdateDto requestDto = new SubjectCreateUpdateDto();

        List<String> teacherEmails = new ArrayList<>();
        teacherEmails.add("teacher@gmail.com");
        requestDto.setTeacherEmails(teacherEmails);
        requestDto.setTitle("Computer Science");

        when(service.addOne(any(SubjectCreateUpdateDto.class))).thenReturn(subject);
        when(specializationMapper.toResponseDto(subject)).thenReturn(responseDto);

        mvc.perform(post("/api/v1/subjects").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Computer Science"));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createSubject());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/v1/subjects/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Subject subject = createSubject();
        SubjectResponseDto responseDto = createResponseDto();

        SubjectCreateUpdateDto requestDto = new SubjectCreateUpdateDto();
        List<String> teacherEmails = new ArrayList<>();
        teacherEmails.add("teacher@gmail.com");
        requestDto.setTeacherEmails(teacherEmails);
        requestDto.setTitle("Computer Science");

        when(service.fullUpdate(any(SubjectCreateUpdateDto.class), eq(1))).thenReturn(subject);
        when(specializationMapper.toResponseDto(subject)).thenReturn(responseDto);

        mvc.perform(put("/api/v1/subjects/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Computer Science"));

        verify(service).fullUpdate(any(SubjectCreateUpdateDto.class), eq(1));
        verify(specializationMapper).toResponseDto(subject);

    }

}
