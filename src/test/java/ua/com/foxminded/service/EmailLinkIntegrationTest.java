package ua.com.foxminded.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.MainApp;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.entity.Faculty;
import ua.com.foxminded.entity.Teacher;
import ua.com.foxminded.service.suppport.TestDataFactory;

@Testcontainers
@Transactional
@SpringBootTest(classes = { MainApp.class })
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({ "test" })
@ExtendWith(SpringExtension.class)
class EmailLinkIntegrationTest {

    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private ActivationUserService activationUserService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    void activationEmail_shouldContainLink_thatIsHandledByRealController() throws Exception {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Teacher teacher = testDataFactory.persistTeacher(department);

        activationUserService.sendResetEmail(teacher);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        String link = extractLink(captor.getValue());
        assertNotNull(link, "Activation link was not found in the email body");

        String pathAndQuery = toPathAndQuery(link);

        mockMvc.perform(get(pathAndQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/activation"));
    }

    @Test
    void passwordResetEmail_shouldContainLink_thatIsHandledByRealController() throws Exception {
        Faculty faculty = testDataFactory.persistFaculty();
        AcademicDepartment department = testDataFactory.persistDepartment(faculty);
        Teacher teacher = testDataFactory.persistTeacher(department);

        activationUserService.requestPasswordReset(teacher.getEmail());

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        String link = extractLink(captor.getValue());
        assertNotNull(link, "Password reset link was not found in the email body");

        String pathAndQuery = toPathAndQuery(link);

        mockMvc.perform(get(pathAndQuery))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/reset-password"));
    }

    private String extractLink(SimpleMailMessage message) {
        String text = message.getText();
        assertNotNull(text, "Email body was empty");

        Matcher matcher = URL_PATTERN.matcher(text);
        assertTrue(matcher.find(), "No URL found in email body: " + text);

        // email templates wrap the link in "**...**" (see email.activation.text /
        // email.reset.text), so trailing markdown asterisks need to be stripped
        return matcher.group().replaceAll("\\*+$", "");
    }

    private String toPathAndQuery(String link) {
        URI uri = URI.create(link);
        String query = uri.getRawQuery();
        return query == null ? uri.getRawPath() : uri.getRawPath() + "?" + query;
    }
}