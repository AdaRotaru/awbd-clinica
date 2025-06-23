package com.awbd.clinica.service;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.domain.Speciality;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.repository.ClinicRepository;
import com.awbd.clinica.repository.DoctorRepository;
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
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;

    @BeforeEach
    void setup() {
        Clinic clinic = Clinic.builder().id(1L).name("Clinic A").address("Str. Test 1").build();
        doctor = Doctor.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .speciality(Speciality.RADIOLOG)
                .clinic(clinic)
                .build();
    }

    @Test
    void testCreateDoctor() {
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        Doctor saved = doctorService.createDoctor(doctor);
        assertEquals("John", saved.getFirstName());
    }

    @Test
    void testGetDoctorEntity_Found() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        Doctor result = doctorService.getDoctorEntity(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetDoctorEntity_NotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> doctorService.getDoctorEntity(1L));
        assertEquals("Doctor with ID 1 not found.", ex.getMessage());
    }

    @Test
    void testUpdateDoctor_Found() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        Doctor updated = doctorService.updateDoctor(doctor);
        assertEquals("John", updated.getFirstName());
    }

    @Test
    void testUpdateDoctor_NotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> doctorService.updateDoctor(doctor));
    }

    @Test
    void testDeleteDoctor_Found() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        doctorService.deleteDoctor(1L);
        verify(doctorRepository).delete(doctor);
    }

    @Test
    void testDeleteDoctor_NotFound() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> doctorService.deleteDoctor(1L));
    }

    @Test
    void testGetAllDoctorsPage() {
        PageRequest request = PageRequest.of(0, 10);
        when(doctorRepository.findAll(request)).thenReturn(new PageImpl<>(List.of(doctor)));
        Page<Doctor> result = doctorService.getAllDoctorsPage(request);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAllDoctors() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));
        List<Doctor> doctors = doctorService.getAllDoctors();
        assertEquals(1, doctors.size());
    }
}
