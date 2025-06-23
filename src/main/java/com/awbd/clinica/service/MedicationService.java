package com.awbd.clinica.service;

import com.awbd.clinica.domain.Medication;
import com.awbd.clinica.dto.MedicationDto;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.mapper.MedicationMapper;
import com.awbd.clinica.repository.MedicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    public MedicationService(MedicationRepository medicationRepository,
                             MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }



    public Medication createMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Medication getMedication(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medication with ID " + id + " not found."));
    }

    public List<Medication> getAllMedications(String brand) {
        if (brand != null && !brand.isBlank()) {
            return medicationRepository.findByBrandContainingIgnoreCase(brand);
        }
        return medicationRepository.findAll();
    }

    public Medication updateMedication(Medication medication) {
        Medication existing = medicationRepository.findById(medication.getId())
                .orElseThrow(() -> new NotFoundException("Medication with ID " + medication.getId() + " not found."));
        return medicationRepository.save(medication);
    }

    public void deleteMedication(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medication with ID " + id + " not found."));
        medicationRepository.delete(medication);
    }
}
