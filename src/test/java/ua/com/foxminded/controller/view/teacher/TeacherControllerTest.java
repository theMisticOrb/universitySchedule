package ua.com.foxminded.controller.view.teacher;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import ua.com.foxminded.dto.lesson.LessonResponseDto;
import ua.com.foxminded.dto.user.TeacherResponseDto;
import ua.com.foxminded.entity.Classroom;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.ClassHour;
import ua.com.foxminded.enums.WeekDay;
import ua.com.foxminded.mapper.LessonMapper;
import ua.com.foxminded.mapper.TeacherMapper;
import ua.com.foxminded.repository.ClassroomRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.repository.SubjectRepository;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.LessonService;
import ua.com.foxminded.service.TeacherService;

@WebMvcTest(controllers = TeacherController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class TeacherControllerTest {

    private static final int TEACHER_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private SubjectRepository subjectRepository;

    @MockBean
    private ClassroomRepository classroomRepository;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private TeacherService service;

    @MockBean
    private TeacherMapper mapper;

    @MockBean
    private LessonMapper lessonMapper;

    private Teacher teacherPrincipal;
    private Authentication teacherAuthentication;

    private void initTeacherAuthentication() {
        teacherPrincipal = mock(Teacher.class);
        when(teacherPrincipal.getId()).thenReturn(TEACHER_ID);

        CustomUserDetails userDetails = new CustomUserDetails(teacherPrincipal);
        teacherAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_TEACHER")));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void listTeachers_shouldReturnViewWithMappedTeachers_whenAuthenticated() throws Exception {
        Teacher teacher = new Teacher();
        TeacherResponseDto dto = new TeacherResponseDto();
        when(service.findAll()).thenReturn(Collections.singletonList(teacher));
        when(mapper.toResponseDto(teacher)).thenReturn(dto);

        mockMvc.perform(get("/teacher")).andExpect(status().isOk()).andExpect(view().name("teacher/list"))
                .andExpect(model().attributeExists("teachers"));

        verify(service, times(1)).findAll();
        verify(mapper, times(1)).toResponseDto(teacher);
    }

    @Test
    void listTeachers_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/teacher")).andExpect(status().is3xxRedirection()).andExpect(result -> {
            String location = result.getResponse().getHeader("Location");
            assertNotNull(location);
            assertTrue(location.contains("/login"));
        });
    }

    @Test
    void dashboard_shouldReturnViewWithCurrentUser() throws Exception {
        initTeacherAuthentication();

        mockMvc.perform(get("/teacher/dashboard").with(authentication(teacherAuthentication)))
                .andExpect(status().isOk()).andExpect(view().name("teacher/dashboard"))
                .andExpect(model().attribute("currentUser", teacherPrincipal));
    }

    @Test
    void schedule_shouldReturnViewWithScheduleAndEnums() throws Exception {
        initTeacherAuthentication();

        mockMvc.perform(get("/teacher/schedule").with(authentication(teacherAuthentication))).andExpect(status().isOk())
                .andExpect(view().name("teacher/schedule")).andExpect(model().attributeExists("schedule"))
                .andExpect(model().attribute("weekDays", WeekDay.values()))
                .andExpect(model().attribute("classHours", ClassHour.values()));

        verify(lessonService, times(1)).getWeeklyScheduleForTeacher(TEACHER_ID);
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getGroupSchedule_shouldReturnMappedScheduleGroupedByDay() throws Exception {
        LessonResponseDto lessonDto = new LessonResponseDto();

        Map<WeekDay, List<LessonResponseDto>> schedule = new LinkedHashMap<>();
        schedule.put(WeekDay.MONDAY, Collections.singletonList(lessonDto));

        when(lessonService.getWeeklyScheduleForGroup(1)).thenReturn(schedule);

        mockMvc.perform(get("/teacher/schedule/group").param("id", "1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.MONDAY").isArray()).andExpect(jsonPath("$.MONDAY.length()").value(1));

        verify(lessonService, times(1)).getWeeklyScheduleForGroup(1);
    }

    @Test
    void showAddLessonForm_shouldReturnViewWithAllFormData() throws Exception {
        initTeacherAuthentication();
        when(subjectRepository.findByTeacherId(TEACHER_ID)).thenReturn(Collections.emptyList());
        when(classroomRepository.findAll()).thenReturn(Collections.emptyList());
        when(groupRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/teacher/schedule/add").with(authentication(teacherAuthentication)))
                .andExpect(status().isOk()).andExpect(view().name("teacher/add-lesson"))
                .andExpect(model().attributeExists("subjects", "classrooms", "groups", "weekDays", "classHours"));

        verify(subjectRepository, times(1)).findByTeacherId(TEACHER_ID);
    }

    @Test
    void addLesson_shouldRedirectToSchedule_whenCreationSucceeds() throws Exception {
        initTeacherAuthentication();

        mockMvc.perform(post("/teacher/schedule/add").with(authentication(teacherAuthentication)).with(csrf()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/teacher/schedule"));

        verify(lessonService, times(1)).addOne(any(), eq(TEACHER_ID));
    }

    @Test
    void addLesson_shouldReRenderFormWithError_whenServiceThrowsIllegalArgument() throws Exception {
        initTeacherAuthentication();
        doThrow(new IllegalArgumentException("Classroom is already occupied")).when(lessonService).addOne(any(),
                eq(TEACHER_ID));

        mockMvc.perform(post("/teacher/schedule/add").with(authentication(teacherAuthentication)).with(csrf()))
                .andExpect(status().isOk()).andExpect(view().name("teacher/add-lesson"))
                .andExpect(model().attribute("error", "Classroom is already occupied"));
    }

    @Test
    void addLesson_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        initTeacherAuthentication();

        mockMvc.perform(post("/teacher/schedule/add").with(authentication(teacherAuthentication)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getFreeClassrooms_shouldReturnOnlyNamesOfUnoccupiedClassrooms() throws Exception {
        Classroom occupied = new Classroom();
        occupied.setId(1);
        occupied.setName("Room 101");

        Classroom free = new Classroom();
        free.setId(2);
        free.setName("Room 102");

        when(lessonService.findOccupiedClassroomIds(eq(WeekDay.MONDAY), eq(ClassHour.FIRST_CLASS)))
                .thenReturn(Collections.singleton(1));
        when(classroomRepository.findAll()).thenReturn(java.util.Arrays.asList(occupied, free));

        mockMvc.perform(get("/teacher/schedule/classrooms/free").param("weekDay", "MONDAY").param("classHour", "FIRST_CLASS"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0]").value("Room 102"));
        
    }

}
