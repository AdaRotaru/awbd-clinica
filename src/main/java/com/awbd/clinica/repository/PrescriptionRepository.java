package com.awbd.clinica.repository;

import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.domain.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Optional<Prescription> findByAppointment(Appointment appointment);
    Optional<Prescription> findByAppointmentId(Long appointmentId);

}
