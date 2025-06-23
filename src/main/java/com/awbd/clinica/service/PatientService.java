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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;

    public PatientService(PatientRepository patientRepository,
                          AppointmentRepository appointmentRepository,
                          PatientMapper patientMapper,
                          AppointmentMapper appointmentMapper) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientMapper = patientMapper;
        this.appointmentMapper = appointmentMapper;
    }


    public PatientDto getPatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + id + " not found."));
        return patientMapper.patientToDto(patient);
    }


    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient getPatientEntity(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + id + " not found."));
    }

    public Patient updatePatient(Patient patient) {
        Patient existing = patientRepository.findById(patient.getId())
                .orElseThrow(() -> new NotFoundException("Patient with ID " + patient.getId() + " not found."));
        return patientRepository.save(patient);
    }

    public Page<Patient> getAllPatients(PageRequest pageRequest) {
        return patientRepository.findAll(pageRequest);
    }

    public List<Appointment> getPatientUpcomingAppointments(Long id) {
        LocalDateTime now = LocalDateTime.now();
        return appointmentRepository.findByPatientIdAndStartDateAfter(id, now);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient with ID " + id + " not found."));

        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        appointmentRepository.deleteAll(appointments);

        patientRepository.delete(patient);
    }
}
