package com.awbd.clinica.service;

import com.awbd.clinica.domain.*;
import com.awbd.clinica.dto.AppointmentDto;
import com.awbd.clinica.dto.AppointmentRescheduleDto;
import com.awbd.clinica.exception.AppointmentsOverlappingException;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.exception.WrongDatesOrderException;
import com.awbd.clinica.mapper.AppointmentMapper;
import com.awbd.clinica.repository.AppointmentRepository;
import com.awbd.clinica.repository.DoctorRepository;
import com.awbd.clinica.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentService appointmentService;

    private Patient patient;
    private Doctor doctor;
    private AppointmentDto appointmentDto;
    private Appointment appointment;
    private LocalDateTime start, end;

    @BeforeEach
    void setup() {
        patient = Patient.builder()
                .id(1L)
                .firstName("Ada")
                .lastName("Pop")
                .email("ada@test.com")
                .phone("123")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        doctor = Doctor.builder()
                .id(2L)
                .firstName("Doc")
                .lastName("Tor")
                .speciality(Speciality.RADIOLOG)
                .build();

        start = LocalDateTime.now().plusDays(1);
        end = start.plusMinutes(30);

        appointmentDto = new AppointmentDto(1L, 2L, start, end, "comentarii");
        appointment = Appointment.builder()
                .id(3L)
                .patient(patient)
                .doctor(doctor)
                .startDate(start)
                .endDate(end)
                .comments("comentarii")
                .build();
    }

    @Test
    void testCreateAppointment_Success() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointment);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findAll()).thenReturn(List.of());
        when(appointmentRepository.save(any())).thenReturn(appointment);

        Appointment result = appointmentService.createAppointment(appointmentDto);

        assertNotNull(result);
        assertEquals(appointment.getComments(), result.getComments());
    }

    @Test
    void testCreateAppointment_DoctorNotFound() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointment);
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));
    }

    @Test
    void testCreateAppointment_PatientNotFound() {
        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointment);
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> appointmentService.createAppointment(appointmentDto));
    }

    @Test
    void testCreateAppointment_Overlapping() {
        Appointment overlapping = Appointment.builder()
                .startDate(start.minusMinutes(15))
                .endDate(end.plusMinutes(15))
                .doctor(doctor)
                .patient(patient)
                .build();

        when(appointmentMapper.appointmentDtoToAppointment(appointmentDto)).thenReturn(appointment);
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAll()).thenReturn(List.of(overlapping));

        assertThrows(AppointmentsOverlappingException.class, () -> appointmentService.createAppointment(appointmentDto));
    }


    @Test
    void testGetAppointment_Success() {
        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(appointment));
        Appointment result = appointmentService.getAppointment(3L);
        assertEquals(3L, result.getId());
    }

    @Test
    void testGetAppointment_NotFound() {
        when(appointmentRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> appointmentService.getAppointment(3L));
    }

    @Test
    void testDeleteAppointment_Success() {
        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(appointment));
        appointmentService.deleteAppointment(3L);
        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void testDeleteAppointment_NotFound() {
        when(appointmentRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> appointmentService.deleteAppointment(3L));
    }

    @Test
    void testRescheduleAppointment_InvalidDates() {
        AppointmentRescheduleDto dto = new AppointmentRescheduleDto(end, start); // end < start

        when(appointmentRepository.findById(3L)).thenReturn(Optional.of(appointment));

        assertThrows(WrongDatesOrderException.class, () -> appointmentService.rescheduleAppointment(3L, dto));
    }

}
