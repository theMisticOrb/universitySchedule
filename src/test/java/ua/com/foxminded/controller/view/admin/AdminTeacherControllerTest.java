package ua.com.foxminded.controller.view.admin;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.AcademicDepartmentService;
import ua.com.foxminded.service.SubjectService;
import ua.com.foxminded.service.TeacherService;

@WebMvcTest(controllers = AdminTeacherController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class AdminTeacherControllerTest {

    private static final int TEACHER_ID = 1;
    private static final int DEPARTMENT_ID = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private AcademicDepartmentService departmentService;

    @MockBean
    private SubjectService subjectService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findTeachersAndDepartments_shouldReturnViewWithAllData_whenUserIsAdmin() throws Exception {
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(departmentService.findAll()).thenReturn(Collections.emptyList());
        when(subjectService.findAll()).thenReturn(Collections.emptyList());
 
        mockMvc.perform(get("/admin/teachers"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/teachers"))
            .andExpect(model().attributeExists("teachers", "departments", "allSubjects"));
 
        verify(teacherService, times(1)).findAll();
        verify(departmentService, times(1)).findAll();
        verify(subjectService, times(1)).findAll();
    }

    @Test
    void findTeachersAndDepartments_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/admin/teachers")).andExpect(status().is3xxRedirection())
                .andExpect(header -> header.getResponse().getRedirectedUrl().contains("/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void findTeachersAndDepartments_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(get("/admin/teachers")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeTeacherDepartment_shouldRedirectToTeachersList_whenUpdateSucceeds() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/department", TEACHER_ID).with(csrf()).param("departmentId",
                String.valueOf(DEPARTMENT_ID))).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/teachers"));

        verify(teacherService, times(1)).changeTeacherDepartment(TEACHER_ID, DEPARTMENT_ID);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeTeacherDepartment_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/department", TEACHER_ID).param("departmentId",
                String.valueOf(DEPARTMENT_ID))).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void changeTeacherDepartment_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/department", TEACHER_ID).with(csrf()).param("departmentId",
                String.valueOf(DEPARTMENT_ID))).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showAssignSubjectsForm_shouldReturnViewWithTeacherAndData_whenUserIsAdmin() throws Exception {
        Teacher teacher = mock(Teacher.class);
        when(teacherService.findById(TEACHER_ID)).thenReturn(teacher);
        when(departmentService.findAll()).thenReturn(Collections.emptyList());
        when(subjectService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/teachers/{id}/subjects", TEACHER_ID)).andExpect(status().isOk())
                .andExpect(view().name("admin/teacher-subjects")).andExpect(model().attribute("teacher", teacher))
                .andExpect(model().attributeExists("departments", "subjectsData"));

        verify(teacherService, times(1)).findById(TEACHER_ID);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void showAssignSubjectsForm_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(get("/admin/teachers/{id}/subjects", TEACHER_ID)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignSubjectsBatch_shouldReturnOkWithRedirect_whenAssignmentSucceeds() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/subjects/batch", TEACHER_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"subjectTitles\": [\"Math\", \"Physics\"]}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.redirect").value("/admin/teachers"));

        verify(teacherService, times(1)).assignSubjects(TEACHER_ID, Arrays.asList("Math", "Physics"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignSubjectsBatch_shouldReturnBadRequest_whenSubjectTitlesKeyMissing() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/subjects/batch", TEACHER_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());

        verify(teacherService, times(0)).assignSubjects(anyInt(), anyList());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignSubjectsBatch_shouldReturnBadRequest_whenSubjectTitlesIsEmpty() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/subjects/batch", TEACHER_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"subjectTitles\": []}"))
                .andExpect(status().isBadRequest());

        verify(teacherService, times(0)).assignSubjects(anyInt(), anyList());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignSubjectsBatch_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/subjects/batch", TEACHER_ID).contentType(MediaType.APPLICATION_JSON)
                .content("{\"subjectTitles\": []}")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void assignSubjectsBatch_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/subjects/batch", TEACHER_ID).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"subjectTitles\": []}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeSubject_shouldRedirectToTeacherSubjectsPage_whenRemovalSucceeds() throws Exception {
        mockMvc.perform(
                post("/admin/teachers/{id}/subjects/remove", TEACHER_ID).with(csrf()).param("subjectTitle", "Math"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/teachers/" + TEACHER_ID + "/subjects"));

        verify(teacherService, times(1)).removeSubjectFromTeacher(TEACHER_ID, "Math");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeSubject_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/teachers/{id}/subjects/remove", TEACHER_ID).param("subjectTitle", "Math"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void removeSubject_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(
                post("/admin/teachers/{id}/subjects/remove", TEACHER_ID).with(csrf()).param("subjectTitle", "Math"))
                .andExpect(status().isForbidden());
    }

}
