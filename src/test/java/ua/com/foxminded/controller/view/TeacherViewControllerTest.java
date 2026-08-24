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

import ua.com.foxminded.controller.view.teacher.TeacherController;
import ua.com.foxminded.dto.user.TeacherResponseDto;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.SubjectRepository;
import ua.com.foxminded.service.LessonService;
import ua.com.foxminded.service.TeacherService;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class TeacherViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService service;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private TeacherMapper mapper;

    @MockBean
    private LessonMapper lessonMapper;
    
    @MockBean
    private SubjectRepository subjectRepository;
    
    @MockBean
    private ClassroomRepository classroomRepository;
    
    @MockBean
    private GroupRepository groupRepository;

    @Test
    void listDepartments_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Teacher> teachers = Arrays.asList(new Teacher());
        TeacherResponseDto dto = new TeacherResponseDto();

        when(service.findAll()).thenReturn(teachers);
        when(mapper.toResponseDto(any(Teacher.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/teacher")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("teacher/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"));

        verify(service, times(1)).findAll();

    }
}
