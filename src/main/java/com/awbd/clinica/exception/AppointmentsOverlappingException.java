package com.awbd.clinica.exception;

public class AppointmentsOverlappingException extends RuntimeException {
    public AppointmentsOverlappingException() {
        super("Appointment is overlapping with an already existing appointment.");
    }
}
