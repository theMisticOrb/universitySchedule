package ua.com.foxminded.controller.view.admin;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.service.GroupService;
import ua.com.foxminded.service.StudentService;

@WebMvcTest(controllers = AdminStudentsController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class AdminStudentsControllerTest {

    private static final int STUDENT_ID = 1;
    private static final int GROUP_ID = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private GroupService groupService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findStudentsAndGroups_shouldReturnViewWithStudentsAndGroups_whenUserIsAdmin() throws Exception {
        when(studentService.findAll()).thenReturn(Collections.emptyList());
        when(groupService.findAll()).thenReturn(Collections.emptyList());
 
        mockMvc.perform(get("/admin/students"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/students"))
            .andExpect(model().attributeExists("students"))
            .andExpect(model().attributeExists("groups"));
 
        verify(studentService, times(1)).findAll();
        verify(groupService, times(1)).findAll();
    }

    @Test
    void findStudentsAndGroups_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/admin/students")).andExpect(status().is3xxRedirection())
                .andExpect(header -> header.getResponse().getRedirectedUrl().contains("/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void findStudentsAndGroups_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(get("/admin/students")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeStudentGroup_shouldRedirectToStudentsList_whenUpdateSucceeds() throws Exception {
        mockMvc.perform(
                post("/admin/students/{id}/group", STUDENT_ID).with(csrf()).param("groupId", String.valueOf(GROUP_ID)))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/students"));

        verify(studentService, times(1)).changeStudentGroup(STUDENT_ID, GROUP_ID);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeStudentGroup_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/students/{id}/group", STUDENT_ID).param("groupId", String.valueOf(GROUP_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void changeStudentGroup_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(
                post("/admin/students/{id}/group", STUDENT_ID).with(csrf()).param("groupId", String.valueOf(GROUP_ID)))
                .andExpect(status().isForbidden());
    }

}
