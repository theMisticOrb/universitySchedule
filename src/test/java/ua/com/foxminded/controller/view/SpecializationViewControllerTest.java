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

import ua.com.foxminded.dto.specialization.SpecializationResponseDto;
import ua.com.foxminded.entity.Specialization;
import ua.com.foxminded.mapper.SpecializationMapper;
import ua.com.foxminded.service.SpecializationService;

@WebMvcTest(SpecializationViewController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class SpecializationViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecializationService service;

    @MockBean
    private SpecializationMapper mapper;

    @Test
    void listSpecializations_shouldReturnDepartmentListPage_whenCalled() throws Exception {

        List<Specialization> specializations = Arrays.asList(new Specialization());
        SpecializationResponseDto dto = new SpecializationResponseDto();

        when(service.findAll()).thenReturn(specializations);
        when(mapper.toResponseDto(any(Specialization.class))).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/specializations")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("specialization/list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("specializations"));

        verify(service, times(1)).findAll();

    }
}
