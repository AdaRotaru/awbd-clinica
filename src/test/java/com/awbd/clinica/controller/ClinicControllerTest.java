package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.service.ClinicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(ClinicControllerTest.Config.class)
@WebMvcTest(ClinicController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClinicService clinicService;

    @TestConfiguration
    static class Config {
        @Bean
        public ClinicService clinicService() {
            return mock(ClinicService.class);
        }
    }

    @Test
    void getAllClinics_ShouldReturnViewWithPage() throws Exception {
        Page<Clinic> clinicPage = new PageImpl<>(List.of(new Clinic()));
        when(clinicService.getAllClinics(any(PageRequest.class))).thenReturn(clinicPage);

        mockMvc.perform(get("/clinics"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinics/clinicsList.html"))
                .andExpect(model().attributeExists("page"));
    }

    @Test
    void getClinic_ShouldReturnDetailsPage() throws Exception {
        Clinic clinic = new Clinic();
        when(clinicService.getClinic(1L)).thenReturn(clinic);

        mockMvc.perform(get("/clinics/info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinics/clinicsInfo.html"))
                .andExpect(model().attributeExists("clinic"));
    }

    @Test
    void newClinic_ShouldReturnNewFormPage() throws Exception {
        mockMvc.perform(get("/clinics/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("clinics/clinicsNew.html"))
                .andExpect(model().attributeExists("clinic"));
    }

    @Test
    void createClinic_ValidInput_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/clinics")
                        .param("name", "Test Clinic")
                        .param("address", "Test Address"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clinics"));

        verify(clinicService, times(1)).createClinic(any(Clinic.class));
    }

    @Test
    void deleteClinic_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/clinics/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clinics"));

        verify(clinicService).deleteClinic(1L);
    }
}
