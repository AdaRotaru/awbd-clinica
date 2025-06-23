package com.awbd.clinica.mapper;

import com.awbd.clinica.domain.Medication;
import com.awbd.clinica.dto.MedicationDto;
import org.springframework.stereotype.Component;

@Component
public class MedicationMapper {

    public Medication dtoToMedication(MedicationDto dto) {
        Medication medication = new Medication();
        medication.setName(dto.getName());
        medication.setBrand(dto.getBrand());
        return medication;
    }

    public MedicationDto medicationToDto(Medication medication) {
        return MedicationDto.builder()
                .name(medication.getName())
                .brand(medication.getBrand())
                .build();
    }
}
