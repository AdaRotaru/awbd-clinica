package com.awbd.clinica.service;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.dto.DoctorDto;
import com.awbd.clinica.exception.NotFoundException;
import com.awbd.clinica.mapper.DoctorMapper;
import com.awbd.clinica.repository.ClinicRepository;
import com.awbd.clinica.repository.DoctorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository,
                         ClinicRepository clinicRepository,
                         DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
        this.doctorMapper = doctorMapper;
    }


    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorEntity(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + id + " not found."));
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Doctor doctor) {
        Doctor existing = doctorRepository.findById(doctor.getId())
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + doctor.getId() + " not found."));
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor with ID " + id + " not found."));
        doctorRepository.delete(doctor);
    }
    public Page<Doctor> getAllDoctorsPage(PageRequest pageRequest) {
        return doctorRepository.findAll(pageRequest);
    }
}
