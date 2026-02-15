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

import ua.com.foxminded.dto.subject.SubjectResponseDto;
import ua.com.foxminded.entity.Subject;
import ua.com.foxminded.mapper.SubjectMapper;
import ua.com.foxminded.service.SubjectService;

@WebMvcTest(SubjectViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class SubjectViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService service;

    @MockBean
    private SubjectMapper mapper;

    @Test
    void listSubjects_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Subject> subjects = Arrays.asList(new Subject());
        SubjectResponseDto dto = new SubjectResponseDto();

        when(service.findAll()).thenReturn(subjects);
        when(mapper.toResponseDto(any(Subject.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/subjects")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("subject/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("subjects"));

        verify(service, times(1)).findAll();

    }
}
