package com.awbd.clinica.mapper;


import org.springframework.stereotype.Component;
import com.awbd.clinica.dto.AppointmentDto;
import com.awbd.clinica.domain.Appointment;

@Component
public class AppointmentMapper {
    public Appointment appointmentDtoToAppointment(AppointmentDto appointmentDto) {
        return new Appointment(appointmentDto.getStartDate(), appointmentDto.getEndDate(), appointmentDto.getComments());
    }
    public AppointmentDto appointmentToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .startDate(appointment.getStartDate())
                .endDate(appointment.getEndDate())
                .comments(appointment.getComments())
                .doctorId(appointment.getDoctor().getId())
                .patientId(appointment.getPatient().getId())
                .build();
    }
}
