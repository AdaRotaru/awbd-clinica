package com.awbd.clinica.service;

import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.domain.Patient;
import com.awbd.clinica.dto.AppointmentDto;
import com.awbd.clinica.dto.AppointmentRescheduleDto;
import com.awbd.clinica.exception.AppointmentsOverlappingException;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.exception.WrongDatesOrderException;
import com.awbd.clinica.mapper.AppointmentMapper;
import com.awbd.clinica.repository.AppointmentRepository;
import com.awbd.clinica.repository.DoctorRepository;
import com.awbd.clinica.repository.PatientRepository;
import com.awbd.clinica.repository.PrescriptionRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, PrescriptionRepository prescriptionRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentMapper = appointmentMapper;
    }

    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Long doctorId = appointmentDto.getDoctorId();
        Long patientId = appointmentDto.getPatientId();

        Appointment appointment = appointmentMapper.appointmentDtoToAppointment(appointmentDto);

        // check if doctor exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + doctorId + " not found."));
        appointment.setDoctor(doctor);

        // check if patient exists
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + patientId + " not found."));
        appointment.setPatient(patient);

        //check for date in wrong order
        boolean bWrongDatesOrder = appointment.getStartDate().isAfter(appointment.getEndDate());
        if (bWrongDatesOrder) {
            throw new WrongDatesOrderException();
        }

        // check for overlapping appointments
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> timeOverlappingAppointments = appointments.stream()
                .filter(appointment1 -> appointment1.getStartDate().isBefore(appointment.getEndDate()) && appointment1.getEndDate().isAfter(appointment.getStartDate())).collect(Collectors.toList());
        boolean bOverlappingDoctorOrPatient = timeOverlappingAppointments.stream().anyMatch(appointment1 -> appointment1.getDoctor().getId() == doctorId || appointment1.getPatient().getId() == patientId);

        if (bOverlappingDoctorOrPatient) {
            throw new AppointmentsOverlappingException();
        }

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + id + " not found."));
    }

    public List<Appointment> getAllAppointments(Long patientId, Long doctorId) {
        // create example object
        Appointment appointment = new Appointment();

        //check if patient exists
        if (patientId != null) {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new NotFoundException("Patient with ID " + patientId + " not found"));

            appointment.setPatient(patient);
        }

        // check if doctor exists
        if (doctorId != null) {
            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new NotFoundException("Doctor with ID " + doctorId + " not found."));

            appointment.setDoctor(doctor);
        }

        // ignore id field
        // null fields will be ignored by default
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        return appointmentRepository.findAll(Example.of(appointment, matcher));
    }

    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + id + " not found."));
        appointmentRepository.delete(appointment);
    }

    public Appointment rescheduleAppointment(Long id, AppointmentRescheduleDto appointmentRescheduleDto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + id + " not found."));

        appointment.setEndDate(appointmentRescheduleDto.getEndDate());
        appointment.setStartDate(appointmentRescheduleDto.getStartDate());

        // check for date in wrong order
        boolean bWrongDatesOrder = appointment.getStartDate().isAfter(appointment.getEndDate());
        if (bWrongDatesOrder) {
            throw new WrongDatesOrderException();
        }

        long doctorId = appointment.getDoctor().getId();
        long patientId = appointment.getPatient().getId();

        // check for overlapping appointments
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> timeOverlappingAppointments = appointments.stream()
                .filter(appointment1 -> appointment1.getStartDate().isBefore(appointment.getEndDate()) && appointment1.getEndDate().isAfter(appointment.getStartDate()) && appointment1.getId() != appointment.getId()).collect(Collectors.toList());
        boolean bOverlappingDoctorOrPatient = timeOverlappingAppointments.stream().anyMatch(appointment1 -> appointment1.getDoctor().getId() == doctorId || appointment1.getPatient().getId() == patientId);

        if (bOverlappingDoctorOrPatient) {
            throw new AppointmentsOverlappingException();
        }

        return appointmentRepository.save(appointment);

    }
}