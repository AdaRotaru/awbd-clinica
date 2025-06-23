package com.awbd.clinica.service;

import com.awbd.clinica.domain.Medication;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private MedicationService medicationService;

    private Medication medication;

    @BeforeEach
    void setup() {
        medication = Medication.builder()
                .id(1L)
                .name("Paracetamol")
                .brand("Terapia")
                .build();
    }

    @Test
    void testCreateMedication() {
        when(medicationRepository.save(medication)).thenReturn(medication);
        Medication saved = medicationService.createMedication(medication);
        assertEquals("Paracetamol", saved.getName());
    }

    @Test
    void testGetMedication_Found() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        Medication result = medicationService.getMedication(1L);
        assertEquals("Terapia", result.getBrand());
    }

    @Test
    void testGetMedication_NotFound() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> medicationService.getMedication(1L));
    }

    @Test
    void testGetAllMedications_NoBrand() {
        when(medicationRepository.findAll()).thenReturn(List.of(medication));
        List<Medication> meds = medicationService.getAllMedications(null);
        assertEquals(1, meds.size());
    }

    @Test
    void testGetAllMedications_WithBrand() {
        when(medicationRepository.findByBrandContainingIgnoreCase("Terapia"))
                .thenReturn(List.of(medication));
        List<Medication> meds = medicationService.getAllMedications("Terapia");
        assertEquals(1, meds.size());
    }

    @Test
    void testUpdateMedication_Found() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(medication)).thenReturn(medication);
        Medication updated = medicationService.updateMedication(medication);
        assertEquals(1L, updated.getId());
    }

    @Test
    void testUpdateMedication_NotFound() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> medicationService.updateMedication(medication));
    }

    @Test
    void testDeleteMedication_Found() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        medicationService.deleteMedication(1L);
        verify(medicationRepository).delete(medication);
    }

    @Test
    void testDeleteMedication_NotFound() {
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> medicationService.deleteMedication(1L));
    }
}
