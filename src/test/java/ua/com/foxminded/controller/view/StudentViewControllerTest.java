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

import ua.com.foxminded.controller.view.student.StudentViewController;
import ua.com.foxminded.dto.user.StudentResponseDto;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.service.StudentService;

@WebMvcTest(StudentViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class StudentViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @MockBean
    private StudentMapper mapper;

    @Test
    void listStudents_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Student> students = Arrays.asList(new Student());
        StudentResponseDto dto = new StudentResponseDto();

        when(service.findAll()).thenReturn(students);
        when(mapper.toResponseDto(any(Student.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/students")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("student/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("students"));

        verify(service, times(1)).findAll();

    }
}
