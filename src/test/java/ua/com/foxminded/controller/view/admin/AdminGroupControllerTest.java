package ua.com.foxminded.controller.view.admin;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.SpecializationRepository;
import ua.com.foxminded.service.GroupService;

@WebMvcTest(controllers = AdminGroupController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class AdminGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private AcademicDepartmentRepository departmentRepository;

    @MockBean
    private SpecializationRepository specializationRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void showForm_shouldReturnViewWithDepartmentsAndSpecializations_whenUserIsAdmin() throws Exception {
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());
        when(specializationRepository.findAll()).thenReturn(Collections.emptyList());
 
        mockMvc.perform(get("/admin/groups/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/add-group"))
            .andExpect(model().attributeExists("departments"))
            .andExpect(model().attributeExists("specializations"));
 
        verify(departmentRepository, times(1)).findAll();
        verify(specializationRepository, times(1)).findAll();
    }

    @Test
    void showForm_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/admin/groups/add")).andExpect(status().is3xxRedirection())
                .andExpect(header -> header.getResponse().getRedirectedUrl().contains("/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void showForm_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(get("/admin/groups/add")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveBatch_shouldReturnOkWithRedirect_whenCreationSucceeds() throws Exception {
        mockMvc.perform(post("/admin/groups/batch").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"list\": []}")).andExpect(status().isOk())
                .andExpect(jsonPath("$.redirect").value("/admin/dashboard"));

        verify(groupService, times(1)).addAll(anyList());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveBatch_shouldReturnBadRequestWithErrorMessage_whenServiceThrows() throws Exception {
        doThrow(new RuntimeException("Something went wrong")).when(groupService).addAll(anyList());

        mockMvc.perform(post("/admin/groups/batch").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"list\": []}")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Something went wrong"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveBatch_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/groups/batch").contentType(MediaType.APPLICATION_JSON).content("{\"list\": []}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void saveBatch_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(post("/admin/groups/batch").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"list\": []}")).andExpect(status().isForbidden());
    }

}
