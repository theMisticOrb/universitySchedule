package ua.com.foxminded.flow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.enums.UserRole;
import ua.com.foxminded.repository.TeacherRepository;
import ua.com.foxminded.service.suppport.PostgresTestContainer;
import ua.com.foxminded.service.suppport.TestDataFactory;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
@ExtendWith(SpringExtension.class)
class RegistrationCompletionFlowTest {

    private static final String RAW_PASSWORD = "Password123!";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        PostgreSQLContainer<?> container = PostgresTestContainer.getInstance();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    private Teacher persistLoginableTeacher(boolean registrationCompleted, UserRole... extraRoles) {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Teacher teacher = testDataFactory.persistTeacher(department);

        teacher.setPassword(passwordEncoder.encode(RAW_PASSWORD));
        teacher.setRegistrationCompleted(registrationCompleted);
        for (UserRole role : extraRoles) {
            teacher.getRoles().add(role);
        }

        return teacherRepository.save(teacher);
    }

    @Test
    void login_shouldRedirectToCompleteRegistration_whenRegistrationNotCompleted() throws Exception {
        Teacher teacher = persistLoginableTeacher(false);

        mockMvc.perform(formLogin("/login").user(teacher.getEmail()).password(RAW_PASSWORD))
                .andExpect(redirectedUrl("/complete_registration"));
    }

    @Test
    void login_shouldRedirectToDashboard_whenRegistrationCompleted() throws Exception {
        Teacher teacher = persistLoginableTeacher(true);

        mockMvc.perform(formLogin("/login").user(teacher.getEmail()).password(RAW_PASSWORD))
                .andExpect(redirectedUrl("/teacher/dashboard"));
    }

    @Test
    void login_shouldSkipRegistrationCheck_whenRoleIsAdmin() throws Exception {
        Teacher admin = persistLoginableTeacher(false, UserRole.ADMIN);

        mockMvc.perform(formLogin("/login").user(admin.getEmail()).password(RAW_PASSWORD))
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    void completeRegistrationForm_shouldBeAccessible_forAuthenticatedIncompleteUser() throws Exception {
        Teacher teacher = persistLoginableTeacher(false);
        MockHttpSession session = loginAndGetSession(teacher.getEmail());

        mockMvc.perform(get("/complete_registration").session(session)).andExpect(status().isOk());
    }

    @Test
    void completeRegistration_shouldUpdateUserAndRedirectToLogin_whenValidRequest() throws Exception {
        Teacher teacher = persistLoginableTeacher(false);
        MockHttpSession session = loginAndGetSession(teacher.getEmail());

        String newEmail = "new-" + teacher.getEmail();

        mockMvc.perform(post("/complete_registration").session(session).with(csrf()).param("email", newEmail)
                .param("password", "NewPassword123!").param("confirmPassword", "NewPassword123!"))
                .andExpect(redirectedUrl("/login?registrationCompleted=true"));

        Teacher updated = teacherRepository.findById(teacher.getId())
                .orElseThrow(() -> new AssertionError("Teacher not found after update"));
        assertTrue(updated.isRegistrationCompleted());
        assertEquals(newEmail, updated.getEmail());
    }

    @Test
    void completeRegistration_shouldReRenderForm_whenPasswordsDoNotMatch() throws Exception {
        Teacher teacher = persistLoginableTeacher(false);
        MockHttpSession session = loginAndGetSession(teacher.getEmail());

        mockMvc.perform(post("/complete_registration").session(session).with(csrf())
                .param("email", "another-" + teacher.getEmail()).param("password", "NewPassword123!")
                .param("confirmPassword", "Mismatch123!")).andExpect(status().isOk());

        Teacher unchanged = teacherRepository.findById(teacher.getId()).orElseThrow(() -> new AssertionError("Teacher not found after update"));
        assertTrue(!unchanged.isRegistrationCompleted());
    }

    private MockHttpSession loginAndGetSession(String email) throws Exception {
        return (MockHttpSession) mockMvc.perform(formLogin("/login").user(email).password(RAW_PASSWORD)).andReturn()
                .getRequest().getSession();
    }
}
