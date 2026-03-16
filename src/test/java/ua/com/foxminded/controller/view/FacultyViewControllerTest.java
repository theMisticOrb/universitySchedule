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

import ua.com.foxminded.dto.faculty.FacultyResponseDto;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.mapper.FacultyMapper;
import ua.com.foxminded.service.FacultyService;

@WebMvcTest(FacultyViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class FacultyViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService service;

    @MockBean
    private FacultyMapper mapper;

    @Test
    void listFaculties_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Faculty> faculties = Arrays.asList(new Faculty());
        FacultyResponseDto dto = new FacultyResponseDto();

        when(service.findAll()).thenReturn(faculties);
        when(mapper.toResponseDto(any(Faculty.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculties")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("faculty/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("faculties"));

        verify(service, times(1)).findAll();

    }

}
