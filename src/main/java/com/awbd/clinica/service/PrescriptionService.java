package com.awbd.clinica.service;

import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.domain.Medication;
import com.awbd.clinica.domain.Prescription;
import com.awbd.clinica.dto.PrescriptionDto;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.exception.PrescriptionAlreadyExistingException;
import com.awbd.clinica.mapper.PrescriptionMapper;
import com.awbd.clinica.repository.AppointmentRepository;
import com.awbd.clinica.repository.MedicationRepository;
import com.awbd.clinica.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               AppointmentRepository appointmentRepository,
                               MedicationRepository medicationRepository,
                               PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicationRepository = medicationRepository;
        this.prescriptionMapper = prescriptionMapper;
    }


    public Prescription createPrescription(Prescription prescription) {
        Appointment appointment = prescription.getAppointment();

        if (appointment == null) {
            throw new NotFoundException("Appointment is required.");
        }

        Appointment fullAppointment = appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new NotFoundException("Appointment with ID " + appointment.getId() + " not found."));

        Optional<Prescription> existing = prescriptionRepository.findByAppointment(fullAppointment);
        if (existing.isPresent()) {
            throw new PrescriptionAlreadyExistingException( fullAppointment.getId());
        }

        prescription.setAppointment(fullAppointment);
        return prescriptionRepository.save(prescription);
    }

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescription(Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prescription with ID " + id + " not found."));
    }

    public Prescription updatePrescription(Prescription prescription) {
        Prescription existing = prescriptionRepository.findById(prescription.getId())
                .orElseThrow(() -> new NotFoundException("Prescription with ID " + prescription.getId() + " not found."));
        return prescriptionRepository.save(prescription);
    }

    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prescription with ID " + id + " not found."));
        prescriptionRepository.delete(prescription);
    }
    public Optional<Prescription> findByAppointmentId(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }
}
