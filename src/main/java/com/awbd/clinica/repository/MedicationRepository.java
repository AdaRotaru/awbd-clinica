package com.awbd.clinica.repository;

import com.awbd.clinica.domain.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByBrandContainingIgnoreCase(String brand);

}
