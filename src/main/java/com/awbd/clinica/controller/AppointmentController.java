package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Prescription;
import com.awbd.clinica.service.PrescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.dto.AppointmentDto;
import com.awbd.clinica.dto.AppointmentRescheduleDto;
import com.awbd.clinica.exception.AppointmentsOverlappingException;
import com.awbd.clinica.exception.WrongDatesOrderException;
import com.awbd.clinica.service.AppointmentService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;

    @GetMapping("/new")
    public String createAppointment(Model model) {
        log.info("GET /appointments/new – formular creare programare");
        model.addAttribute("appointmentDto", new AppointmentDto());
        return "appointments/appointmentsNew";
    }

    @PostMapping
    public String createAppointment(@Valid @ModelAttribute AppointmentDto appointmentDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /appointments – eroare validare: {}", bindingResult.getAllErrors());
            return "appointments/appointmentsNew.html";
        }

        try {
            log.info("POST /appointments – creare programare: {}", appointmentDto);
            Appointment createdAppointment = appointmentService.createAppointment(appointmentDto);
            return "redirect:/appointments";

        } catch (AppointmentsOverlappingException | WrongDatesOrderException exception) {
            log.warn("POST /appointments – excepție business: {}", exception.getMessage());
            bindingResult.rejectValue("startDate", "wrongAppointmentDates", exception.getMessage());
            return "appointments/appointmentsNew.html";
        }
    }

    @GetMapping("/info/{id}")
    public String getAppointmentDetails(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentService.getAppointment(id);
        Prescription prescription = prescriptionService.findByAppointmentId(id).orElse(null);

        model.addAttribute("object", appointment);
        model.addAttribute("prescription", prescription);
        return "appointments/appointmentsInfo.html";
    }

    @GetMapping
    public String getAllAppointments(Model model,
                                     @RequestParam(required = false) Long doctorId,
                                     @RequestParam(required = false) Long patientId) {
        log.info("GET /appointments – listare, doctorId={}, patientId={}", doctorId, patientId);
        List<Appointment> appointments = appointmentService.getAllAppointments(patientId, doctorId);
        model.addAttribute("appointments", appointments);
        return "appointments/appointmentsList.html";
    }

    @GetMapping("/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id, Model model) {
        log.info("GET /appointments/reschedule/{} – formular reprogramare", id);
        model.addAttribute("id", id);
        model.addAttribute("appointmentRescheduleDto", new AppointmentRescheduleDto());
        return "appointments/appointmentsReschedule.html";
    }

    @PostMapping("/reschedule/{id}")
    public String rescheduleAppointment(@PathVariable Long id,
                                        @Valid @ModelAttribute AppointmentRescheduleDto appointmentRescheduleDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /appointments/reschedule/{} – eroare validare: {}", id, bindingResult.getAllErrors());
            return "appointments/appointmentsReschedule.html";
        }

        try {
            log.info("POST /appointments/reschedule/{} – reprogramare: {}", id, appointmentRescheduleDto);
            Appointment createdAppointment = appointmentService.rescheduleAppointment(id, appointmentRescheduleDto);
            return "redirect:/appointments/info/" + id;

        } catch (AppointmentsOverlappingException | WrongDatesOrderException exception) {
            log.warn("POST /appointments/reschedule/{} – excepție business: {}", id, exception.getMessage());
            bindingResult.rejectValue("startDate", "wrongAppointmentDates", exception.getMessage());
            return "appointments/appointmentsReschedule.html";
        }
    }

    @RequestMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        log.info("DELETE /appointments/{} – ștergere programare", id);
        appointmentService.deleteAppointment(id);
        return "redirect:/appointments";
    }
}
