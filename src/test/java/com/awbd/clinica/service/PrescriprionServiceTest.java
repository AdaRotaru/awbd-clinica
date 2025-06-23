package com.awbd.clinica.service;

import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.domain.Medication;
import com.awbd.clinica.domain.Prescription;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.exception.PrescriptionAlreadyExistingException;
import com.awbd.clinica.repository.AppointmentRepository;
import com.awbd.clinica.repository.MedicationRepository;
import com.awbd.clinica.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Appointment appointment;
    private Prescription prescription;

    @BeforeEach
    void setup() {
        appointment = Appointment.builder()
                .id(1L)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(1).plusHours(1))
                .comments("consultație")
                .build();

        prescription = Prescription.builder()
                .id(2L)
                .appointment(appointment)
                .comments("luați zilnic")
                .medications(List.of())
                .build();
    }

    @Test
    void testCreatePrescription_Success() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(prescriptionRepository.findByAppointment(appointment)).thenReturn(Optional.empty());
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);

        Prescription result = prescriptionService.createPrescription(prescription);

        assertEquals("luați zilnic", result.getComments());
        verify(prescriptionRepository).save(prescription);
    }

    @Test
    void testCreatePrescription_AlreadyExists() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(prescriptionRepository.findByAppointment(appointment)).thenReturn(Optional.of(prescription));

        assertThrows(PrescriptionAlreadyExistingException.class, () -> prescriptionService.createPrescription(prescription));
    }

    @Test
    void testCreatePrescription_AppointmentNotFound() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> prescriptionService.createPrescription(prescription));
    }

    @Test
    void testCreatePrescription_NullAppointment() {
        prescription.setAppointment(null);
        assertThrows(NotFoundException.class, () -> prescriptionService.createPrescription(prescription));
    }

    @Test
    void testGetAllPrescriptions() {
        when(prescriptionRepository.findAll()).thenReturn(List.of(prescription));
        List<Prescription> result = prescriptionService.getAllPrescriptions();
        assertEquals(1, result.size());
    }

    @Test
    void testGetPrescription_Found() {
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.of(prescription));
        Prescription result = prescriptionService.getPrescription(2L);
        assertEquals(2L, result.getId());
    }

    @Test
    void testGetPrescription_NotFound() {
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prescriptionService.getPrescription(2L));
    }

    @Test
    void testUpdatePrescription_Success() {
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.of(prescription));
        when(prescriptionRepository.save(prescription)).thenReturn(prescription);
        Prescription result = prescriptionService.updatePrescription(prescription);
        assertEquals(2L, result.getId());
    }

    @Test
    void testUpdatePrescription_NotFound() {
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prescriptionService.updatePrescription(prescription));
    }

    @Test
    void testDeletePrescription_Success() {
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.of(prescription));
        prescriptionService.deletePrescription(2L);
        verify(prescriptionRepository).delete(prescription);
    }

    @Test
    void testDeletePrescription_NotFound() {
        when(prescriptionRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> prescriptionService.deletePrescription(2L));
    }
}
