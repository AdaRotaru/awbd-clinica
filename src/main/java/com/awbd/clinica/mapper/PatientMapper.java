package com.awbd.clinica.mapper;

import com.awbd.clinica.domain.Patient;
import com.awbd.clinica.dto.PatientDto;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public Patient dtoToPatient(PatientDto dto) {
        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        return patient;
    }

    public PatientDto patientToDto(Patient patient) {
        return PatientDto.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .phone(patient.getPhone())
                .email(patient.getEmail())
                .build();
    }
}
