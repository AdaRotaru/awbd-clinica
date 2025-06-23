package com.awbd.clinica.mapper;

import com.awbd.clinica.domain.Prescription;
import com.awbd.clinica.dto.PrescriptionDto;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    public Prescription dtoToPrescription(PrescriptionDto dto) {
        Prescription prescription = new Prescription();
        prescription.setComments(dto.getComments());
        return prescription;
    }

    public PrescriptionDto prescriptionToDto(Prescription prescription) {
        return PrescriptionDto.builder()
                .appointmentId(prescription.getAppointment().getId())
                .comments(prescription.getComments())
                .build();
    }
}
