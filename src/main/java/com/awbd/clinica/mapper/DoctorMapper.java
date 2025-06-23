package com.awbd.clinica.mapper;

import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.dto.DoctorDto;
import com.awbd.clinica.domain.Speciality;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor doctorDtoToDoctor(DoctorDto dto) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpeciality(dto.getSpeciality());
        return doctor;
    }

    public DoctorDto doctorToDoctorDto(Doctor doctor) {
        return DoctorDto.builder()
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .speciality(doctor.getSpeciality())
                .clinicId(doctor.getClinic().getId())
                .build();
    }
}
