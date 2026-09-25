package ua.com.foxminded.controller.view.admin;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

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
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.AcademicDepartmentRepository;
import ua.com.foxminded.repository.GroupRepository;
import ua.com.foxminded.service.UserService;

@WebMvcTest(controllers = AdminUsersController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class AdminUsersControllerTest {

    private static final int USER_ID = 1;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private AcademicDepartmentRepository departmentRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void users_shouldReturnViewWithUsers_whenUserIsAdmin() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());
 
        mockMvc.perform(get("/admin/users"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/users"))
            .andExpect(model().attributeExists("users"));
 
        verify(userService, times(1)).findAll();
    }

    @Test
    void users_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/admin/users")).andExpect(status().is3xxRedirection())
                .andExpect(header -> header.getResponse().getRedirectedUrl().contains("/login"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void users_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(get("/admin/users")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editForm_shouldReturnViewWithUserAndRoles_whenUserIsAdmin() throws Exception {
        AppUser user = mock(AppUser.class);
        when(userService.findById(USER_ID)).thenReturn(user);

        mockMvc.perform(get("/admin/users/{id}/edit", USER_ID)).andExpect(status().isOk())
                .andExpect(view().name("admin/user-edit")).andExpect(model().attribute("user", user))
                .andExpect(model().attribute("roles", UserRole.values()));

        verify(userService, times(1)).findById(USER_ID);
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void editForm_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(get("/admin/users/{id}/edit", USER_ID)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeUserRole_shouldRedirectToUsersList_whenUpdateSucceeds() throws Exception {
        mockMvc.perform(post("/admin/users/{id}/edit/roles", USER_ID).with(csrf()).param("roles", "ADMIN", "TEACHER"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/users"));

        Set<UserRole> expectedRoles = EnumSet.of(UserRole.ADMIN, UserRole.TEACHER);
        verify(userService, times(1)).changeRole(USER_ID, expectedRoles);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeUserRole_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/users/{id}/edit/roles", USER_ID).param("roles", "ADMIN"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void changeUserRole_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(post("/admin/users/{id}/edit/roles", USER_ID).with(csrf()).param("roles", "ADMIN"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldRedirectToUsersList_whenDeletionSucceeds() throws Exception {
        mockMvc.perform(post("/admin/users/{id}/delete", USER_ID).with(csrf())).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService, times(1)).deleteById(USER_ID);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(post("/admin/users/{id}/delete", USER_ID)).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void deleteUser_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(post("/admin/users/{id}/delete", USER_ID).with(csrf())).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showAddUserForm_shouldReturnViewWithEmptyFormAndData_whenUserIsAdmin() throws Exception {
        when(groupRepository.findAll()).thenReturn(Collections.emptyList());
        when(departmentRepository.findAll()).thenReturn(Collections.emptyList());
 
        mockMvc.perform(get("/admin/users/add-user"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/add-user"))
            .andExpect(model().attributeExists("addUserForm"))
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attributeExists("departments"));
 
        verify(groupRepository, times(1)).findAll();
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveBatch_shouldReturnOkWithRedirect_whenCreationSucceeds() throws Exception {
        mockMvc.perform(post("/admin/users/add-user/batch").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"users\": []}")).andExpect(status().isOk())
                .andExpect(jsonPath("$.redirect").value("/admin/users"));

        verify(userService, times(1)).createUsers(anyList());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveBatch_shouldReturnBadRequestWithErrorMessage_whenServiceThrows() throws Exception {
        doThrow(new RuntimeException("Email already taken")).when(userService).createUsers(anyList());

        mockMvc.perform(post("/admin/users/add-user/batch").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"users\": []}")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Email already taken"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void saveBatch_shouldReturnForbidden_whenCsrfTokenMissing() throws Exception {
        mockMvc.perform(
                post("/admin/users/add-user/batch").contentType(MediaType.APPLICATION_JSON).content("{\"users\": []}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void saveBatch_shouldReturnForbidden_whenUserLacksAdminRole() throws Exception {
        mockMvc.perform(post("/admin/users/add-user/batch").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"users\": []}")).andExpect(status().isForbidden());
    }

}
