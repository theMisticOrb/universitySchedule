package ua.com.foxminded.controller.view.admin;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.config.SecurityConfig;
import ua.com.foxminded.entity.AppUser;
import ua.com.foxminded.security.CustomUserDetails;
import ua.com.foxminded.service.StudentService;
import ua.com.foxminded.service.TeacherService;
import ua.com.foxminded.service.UserService;

@WebMvcTest(controllers = AdminPanelController.class, excludeAutoConfiguration = ThymeleafAutoConfiguration.class)
@Import(SecurityConfig.class)
class AdminPanelControllerTest {

    private static final long TOTAL_STUDENTS = 42L;
    private static final long TOTAL_TEACHERS = 7L;
    private static final long TOTAL_USERS = 50L;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private TeacherService teacherService;

    private AppUser adminPrincipal;
    private Authentication adminAuthentication;

    private void initAdminAuthentication() {
        adminPrincipal = mock(AppUser.class);

        CustomUserDetails userDetails = new CustomUserDetails(adminPrincipal);
        adminAuthentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void dashboard_shouldReturnViewWithCountsAndCurrentUser_whenAuthenticatedAsAdmin() throws Exception {
        initAdminAuthentication();
        when(studentService.count()).thenReturn(TOTAL_STUDENTS);
        when(teacherService.count()).thenReturn(TOTAL_TEACHERS);
        when(userService.count()).thenReturn(TOTAL_USERS);

        mockMvc.perform(get("/admin/dashboard").with(authentication(adminAuthentication))).andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard")).andExpect(model().attribute("totalStudents", TOTAL_STUDENTS))
                .andExpect(model().attribute("totalTeachers", TOTAL_TEACHERS))
                .andExpect(model().attribute("totalUsers", TOTAL_USERS))
                .andExpect(model().attribute("currentUser", adminPrincipal));

        verify(studentService, times(1)).count();
        verify(teacherService, times(1)).count();
        verify(userService, times(1)).count();
    }

    @Test
    void dashboard_shouldRedirectToLogin_whenAnonymous() throws Exception {
        mockMvc.perform(get("/admin/dashboard")).andExpect(status().is3xxRedirection()).andExpect(result -> {
            String location = result.getResponse().getHeader("Location");
            assertNotNull(location);
            assertTrue(location.contains("/login"));
        });
    }

    @Test
    void dashboard_shouldReturnForbidden_whenAuthenticatedWithoutAdminRole() throws Exception {
        AppUser studentPrincipal = mock(AppUser.class);
        CustomUserDetails studentUserDetails = new CustomUserDetails(studentPrincipal);
        Authentication studentAuthentication = new UsernamePasswordAuthenticationToken(studentUserDetails, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));

        mockMvc.perform(get("/admin/dashboard").with(authentication(studentAuthentication)))
                .andExpect(status().isForbidden());
    }

}
