package com.awbd.clinica.service;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.repository.ClinicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public Clinic createClinic(Clinic clinic) {
        return clinicRepository.save(clinic);
    }


    public Page<Clinic> getAllClinics(PageRequest pageRequest) {
        return clinicRepository.findAll(pageRequest);
    }

    public Clinic getClinic(Long id) {
        return clinicRepository.findById(id).orElseThrow(() -> new RuntimeException("Clinic not found"));
    }

    public void deleteClinic(Long id) {
        clinicRepository.deleteById(id);
    }

    public Clinic updateClinic(Clinic clinic) {
        Optional<Clinic> existingClinic = clinicRepository.findById(clinic.getId());
        if (existingClinic.isEmpty()) {
            throw new RuntimeException("Clinic not found for update");
        }
        return clinicRepository.save(clinic);
    }
    public List<Clinic> getAllClinicsList() {
        return clinicRepository.findAll();
    }

}
