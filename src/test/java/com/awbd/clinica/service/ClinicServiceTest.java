package com.awbd.clinica.service;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.repository.ClinicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicService clinicService;

    private Clinic clinic;

    @BeforeEach
    void setup() {
        clinic = Clinic.builder()
                .id(1L)
                .name("Test Clinic")
                .address("Str. Test 1")
                .build();
    }

    @Test
    void testCreateClinic() {
        when(clinicRepository.save(clinic)).thenReturn(clinic);
        Clinic result = clinicService.createClinic(clinic);
        assertEquals("Test Clinic", result.getName());
    }

    @Test
    void testGetAllClinics() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Clinic> page = new PageImpl<>(List.of(clinic));
        when(clinicRepository.findAll(pageRequest)).thenReturn(page);
        Page<Clinic> result = clinicService.getAllClinics(pageRequest);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetClinic_Found() {
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));
        Clinic result = clinicService.getClinic(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetClinic_NotFound() {
        when(clinicRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> clinicService.getClinic(1L));
        assertEquals("Clinic not found", ex.getMessage());
    }

    @Test
    void testDeleteClinic() {
        clinicService.deleteClinic(1L);
        verify(clinicRepository).deleteById(1L);
    }

    @Test
    void testUpdateClinic_Found() {
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));
        when(clinicRepository.save(clinic)).thenReturn(clinic);
        Clinic updated = clinicService.updateClinic(clinic);
        assertEquals("Test Clinic", updated.getName());
    }

    @Test
    void testUpdateClinic_NotFound() {
        when(clinicRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> clinicService.updateClinic(clinic));
        assertEquals("Clinic not found for update", ex.getMessage());
    }
}
