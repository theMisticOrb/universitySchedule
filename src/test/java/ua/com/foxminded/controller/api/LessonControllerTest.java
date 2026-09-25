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

import ua.com.foxminded.dto.lesson.LessonCreateUpdateDto;
import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.service.LessonService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = LessonRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class LessonControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LessonService service;
    @MockBean
    private LessonMapper lessonMapper;

    @Autowired
    private ObjectMapper mapper;

    private Lesson createLesson() {
        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setSubject(new Subject(1, "Math", null, null, null, null));
        lesson.setWeekDay(WeekDay.FRIDAY);
        return lesson;
    }

    private LessonResponseDto createResponseDto() {
        LessonResponseDto dto = new LessonResponseDto();
        dto.setId(1);
        dto.setSubjectId(1);
        dto.setWeekDay(WeekDay.FRIDAY);
        return dto;
    }

    @Test
    void findAll_shouldReturn200_whenCalled() throws Exception {

        Lesson lesson = createLesson();
        LessonResponseDto responseDto = createResponseDto();

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);

        when(service.findAll()).thenReturn(lessons);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(responseDto);

        mvc.perform(get("/api/v1/lessons").param("subjectId", "1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)).andExpect(jsonPath("$[0].subjectId").value("1"));
    }

    @Test
    void findBySubjectIdAndWeekDay_shouldReturn200_whenCalled() throws Exception {

        LessonResponseDto responseDto = createResponseDto();

        List<LessonResponseDto> lessons = new ArrayList<>();
        lessons.add(responseDto);

        when(service.findBySubjectIdAndWeekDay(1, WeekDay.FRIDAY)).thenReturn(lessons);
        when(lessonMapper.toResponseDto(any(Lesson.class))).thenReturn(responseDto);

        mvc.perform(get("/api/v1/lessons/filter").param("subjectId", "1").param("weekDay", "FRIDAY"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].subjectId").value("1"));
    }

    @Test
    void findById_shouldReturn200_whenCalled() throws Exception {

        Lesson lesson = createLesson();
        LessonResponseDto responseDto = createResponseDto();

        when(service.findById(1)).thenReturn(lesson);
        when(lessonMapper.toResponseDto(lesson)).thenReturn(responseDto);

        mvc.perform(get("/api/v1/lessons/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectId").value(lesson.getSubject().getId()));
    }

    @Test
    void create_shouldReturn201_whenCreated() throws Exception {

        Lesson lesson = createLesson();
        LessonResponseDto responseDto = createResponseDto();
        LessonCreateUpdateDto requestDto = new LessonCreateUpdateDto();
        requestDto.setSubjectId(1);

        when(service.addOne(any(LessonCreateUpdateDto.class), eq(1))).thenReturn(lesson);
        when(lessonMapper.toResponseDto(lesson)).thenReturn(responseDto);

        mvc.perform(post("/api/v1/lessons/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.subjectId").value(lesson.getSubject().getId()));
    }

    @Test
    void delete_shouldReturn204_whenCalled() throws Exception {
        when(service.findById(1)).thenReturn(createLesson());
        doNothing().when(service).deleteById(1);
        mvc.perform(delete("/api/v1/lessons/1")).andExpect(status().isNoContent());
        verify(service).deleteById(1);
    }

    @Test
    void update_shouldReturn200_whenCalled() throws Exception {

        Lesson lesson = createLesson();
        LessonResponseDto responseDto = createResponseDto();
        LessonCreateUpdateDto requestDto = new LessonCreateUpdateDto();
        requestDto.setSubjectId(1);

        when(service.fullUpdate(any(LessonCreateUpdateDto.class), eq(1))).thenReturn(lesson);
        when(lessonMapper.toResponseDto(lesson)).thenReturn(responseDto);

        mvc.perform(put("/api/v1/lessons/1").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto))).andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectId").value(lesson.getSubject().getId()));

    }
}
