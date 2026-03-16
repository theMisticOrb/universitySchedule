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

import ua.com.foxminded.dto.department.AcademicDepartmentResponseDto;
import ua.com.foxminded.entity.AcademicDepartment;
import ua.com.foxminded.mapper.AcademicDepartmentMapper;
import ua.com.foxminded.service.AcademicDepartmentService;

@WebMvcTest(AcademicDepartmentViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class AcademicDepartmentViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcademicDepartmentService service;

    @MockBean
    private AcademicDepartmentMapper mapper;

    @Test
    void listDepartments_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<AcademicDepartment> departments = Arrays.asList(new AcademicDepartment());
        AcademicDepartmentResponseDto dto = new AcademicDepartmentResponseDto();

        when(service.findAll()).thenReturn(departments);
        when(mapper.toResponseDto(any(AcademicDepartment.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/departments")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("department/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("departments"));

        verify(service, times(1)).findAll();

    }

}
