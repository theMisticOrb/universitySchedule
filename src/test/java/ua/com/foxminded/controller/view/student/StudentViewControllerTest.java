package ua.com.foxminded.controller.view.student;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.dto.user.StudentResponseDto;
import ua.com.foxminded.entity.Group;
import ua.com.foxminded.entity.Student;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.mapper.StudentMapper;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.LessonService;
import ua.com.foxminded.service.StudentService;

@WebMvcTest(controllers = StudentViewController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class StudentViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private StudentMapper mapper;

    private Student studentPrincipal;
    private Authentication studentAuthentication;

    private void initStudentAuthentication() {
        studentPrincipal = mock(Student.class);
        when(studentPrincipal.isRegistrationCompleted()).thenReturn(true);
        
        CustomUserDetails userDetails = new CustomUserDetails(studentPrincipal);
        studentAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));
    }

    @Test
    void listStudents_shouldReturnViewWithMappedStudents_whenAuthenticated() throws Exception {
        initStudentAuthentication();
        Student student = new Student();
        StudentResponseDto dto = new StudentResponseDto();
        when(service.findAll()).thenReturn(Collections.singletonList(student));
        when(mapper.toResponseDto(student)).thenReturn(dto);

        mockMvc.perform(get("/student").with(authentication(studentAuthentication))).andExpect(status().isOk()).andExpect(view().name("student/list"))
                .andExpect(model().attributeExists("students"));

        verify(service, times(1)).findAll();
        verify(mapper, times(1)).toResponseDto(student);
    }

    @Test
    void listStudents_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/student")).andExpect(status().is3xxRedirection()).andExpect(result -> {
            String location = result.getResponse().getHeader("Location");
            assertNotNull(location);
            assertTrue(location.contains("/login"));
        });
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void listStudents_shouldReturnForbidden_whenUserLacksStudentOrAdminRole() throws Exception {
        mockMvc.perform(get("/student")).andExpect(status().isForbidden());
    }

    @Test
    void dashboard_shouldReturnViewWithCurrentUser() throws Exception {
        initStudentAuthentication();

        mockMvc.perform(get("/student/dashboard").with(authentication(studentAuthentication)))
                .andExpect(status().isOk()).andExpect(view().name("student/dashboard"))
                .andExpect(model().attribute("currentUser", studentPrincipal));
    }

    @Test
    void schedule_shouldShowErrorAndSkipLessonLookup_whenStudentHasNoGroup() throws Exception {
        initStudentAuthentication();
        when(studentPrincipal.getGroup()).thenReturn(null);

        mockMvc.perform(get("/student/schedule").with(authentication(studentAuthentication))).andExpect(status().isOk())
                .andExpect(view().name("student/schedule-st"))
                .andExpect(model().attribute("error", "You are not yet assigned to any group."))
                .andExpect(model().attribute("weekDays", WeekDay.values()))
                .andExpect(model().attribute("classHours", ClassHour.values()))
                .andExpect(model().attributeDoesNotExist("schedule"));

        verify(lessonService, never()).getWeeklyScheduleForGroup(org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void schedule_shouldReturnGroupSchedule_whenStudentHasGroup() throws Exception {
        initStudentAuthentication();
        Group group = mock(Group.class);
        when(group.getId()).thenReturn(1);
        when(studentPrincipal.getGroup()).thenReturn(group);

        mockMvc.perform(get("/student/schedule").with(authentication(studentAuthentication))).andExpect(status().isOk())
                .andExpect(view().name("student/schedule-st")).andExpect(model().attributeExists("schedule"))
                .andExpect(model().attribute("weekDays", WeekDay.values()))
                .andExpect(model().attribute("classHours", ClassHour.values()))
                .andExpect(model().attributeDoesNotExist("error"));

        verify(lessonService, times(1)).getWeeklyScheduleForGroup(1);
    }

}
