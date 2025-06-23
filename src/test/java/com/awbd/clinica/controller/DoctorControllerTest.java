package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.domain.Speciality;
import com.awbd.clinica.service.DoctorService;
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

@WebMvcTest(DoctorController.class)
@Import(DoctorControllerTest.Config.class)
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DoctorService doctorService;

    @TestConfiguration
    static class Config {
        @Bean
        public DoctorService doctorService() {
            return mock(DoctorService.class);
        }
    }

    @Test
    void getAllDoctors_ShouldReturnDoctorListPage() throws Exception {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpeciality(Speciality.RADIOLOG);

        Page<Doctor> page = new PageImpl<>(List.of(doctor));
        when(doctorService.getAllDoctorsPage(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/doctorsList.html"))
                .andExpect(model().attributeExists("page"));
    }

    @Test
    void getDoctor_ShouldReturnDoctorDetailsPage() throws Exception {
        when(doctorService.getDoctorEntity(1L)).thenReturn(new Doctor());

        mockMvc.perform(get("/doctors/info/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/doctorsInfo.html"))
                .andExpect(model().attributeExists("doctor"));
    }

    @Test
    void newDoctor_ShouldReturnForm() throws Exception {
        mockMvc.perform(get("/doctors/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors/doctorsNew.html"))
                .andExpect(model().attributeExists("doctor"));
    }

    @Test
    void createDoctor_ValidInput_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/doctors")
                        .param("firstName", "John")
                        .param("lastName", "Smith")
                        .param("speciality", "RADIOLOG"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));

        verify(doctorService).createDoctor(any(Doctor.class));
    }

    @Test
    void deleteDoctor_ShouldRedirect() throws Exception {
        mockMvc.perform(get("/doctors/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));

        verify(doctorService).deleteDoctor(1L);
    }
}
