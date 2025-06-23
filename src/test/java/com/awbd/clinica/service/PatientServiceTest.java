package com.awbd.clinica.service;

import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.domain.Patient;
import com.awbd.clinica.dto.AppointmentDto;
import com.awbd.clinica.dto.PatientDto;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.mapper.AppointmentMapper;
import com.awbd.clinica.mapper.PatientMapper;
import com.awbd.clinica.repository.AppointmentRepository;
import com.awbd.clinica.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientMapper patientMapper;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDto patientDto;

    @BeforeEach
    void setup() {
        patient = Patient.builder()
                .id(1L)
                .firstName("Ada")
                .lastName("Test")
                .email("ada@test.com")
                .phone("123")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        patientDto = new PatientDto("Ada", "Test", LocalDate.of(2000, 1, 1), "123", "ada@test.com");

    }

    @Test
    void testGetPatient_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.patientToDto(patient)).thenReturn(patientDto);

        PatientDto result = patientService.getPatient(1L);

        assertEquals("Ada", result.getFirstName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void testGetPatient_NotFound() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> patientService.getPatient(2L));
    }

    @Test
    void testCreatePatient() {
        when(patientRepository.save(patient)).thenReturn(patient);
        Patient result = patientService.createPatient(patient);
        assertEquals("Ada", result.getFirstName());
        verify(patientRepository).save(patient);
    }

    @Test
    void testGetPatientEntity_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        Patient result = patientService.getPatientEntity(1L);
        assertEquals("Ada", result.getFirstName());
    }

    @Test
    void testGetPatientEntity_NotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> patientService.getPatientEntity(99L));
    }

    @Test
    void testUpdatePatient_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);
        Patient updated = patientService.updatePatient(patient);
        assertEquals("Ada", updated.getFirstName());
    }

    @Test
    void testUpdatePatient_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> patientService.updatePatient(patient));
    }

    @Test
    void testGetAllPatients() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Patient> page = new PageImpl<>(List.of(patient));
        when(patientRepository.findAll(pageRequest)).thenReturn(page);

        Page<Patient> result = patientService.getAllPatients(pageRequest);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetPatientUpcomingAppointments() {
        Long patientId = 1L;

        List<Appointment> mockAppointments = List.of(
                Appointment.builder().id(1L).comments("Consultatie").build()
        );

        when(appointmentRepository.findByPatientIdAndStartDateAfter(
                eq(patientId), any(LocalDateTime.class))
        ).thenReturn(mockAppointments);
        List<Appointment> result = patientService.getPatientUpcomingAppointments(patientId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Consultatie", result.get(0).getComments());

        verify(appointmentRepository).findByPatientIdAndStartDateAfter(eq(patientId), any(LocalDateTime.class));
    }


    @Test
    void testDeletePatient_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findByPatientId(1L)).thenReturn(List.of());

        patientService.deletePatient(1L);

        verify(patientRepository).delete(patient);
    }

    @Test
    void testDeletePatient_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> patientService.deletePatient(1L));
    }
}
