package com.awbd.clinica.mapper;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.dto.ClinicDto;
import org.springframework.stereotype.Component;

@Component
public class ClinicMapper {

    public Clinic clinicDtoToClinic(ClinicDto clinicDto) {
        Clinic clinic = new Clinic();
        clinic.setName(clinicDto.getName());
        clinic.setAddress(clinicDto.getAddress());
        return clinic;
    }
}