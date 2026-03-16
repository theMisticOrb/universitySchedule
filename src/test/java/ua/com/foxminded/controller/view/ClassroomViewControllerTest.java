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

import ua.com.foxminded.dto.classroom.ClassroomResponseDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.mapper.ClassroomMapper;
import ua.com.foxminded.service.ClassroomService;

@WebMvcTest(ClassroomViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class ClassroomViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassroomService service;

    @MockBean
    private ClassroomMapper mapper;

    @Test
    void listClassrooms_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Classroom> classrooms = Arrays.asList(new Classroom());
        ClassroomResponseDto dto = new ClassroomResponseDto();

        when(service.findAll()).thenReturn(classrooms);
        when(mapper.toResponseDto(any(Classroom.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/classrooms")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("classroom/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("classrooms"));

        verify(service, times(1)).findAll();

    }

}
