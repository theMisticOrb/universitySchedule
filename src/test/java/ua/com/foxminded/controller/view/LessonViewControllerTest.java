package ua.com.foxminded.controller.view;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.entity.Lesson;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.service.LessonService;

@WebMvcTest(LessonViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class LessonViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService service;

    @MockBean
    private LessonMapper mapper;

    @Test
    void listLessons_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Lesson> lessons = Arrays.asList(new Lesson());
        LessonResponseDto dto = new LessonResponseDto();

        when(service.findAll()).thenReturn(lessons);
        when(mapper.toResponseDto(any(Lesson.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/lessons")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("lesson/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("lessons"));

        verify(service, times(1)).findAll();

    }
}
