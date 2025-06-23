package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Prescription;
import com.awbd.clinica.exception.PrescriptionAlreadyExistingException;
import com.awbd.clinica.service.AppointmentService;
import com.awbd.clinica.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;

    @GetMapping
    public String getAllPrescriptions(Model model) {
        log.info("GET /prescriptions – listare rețete");
        List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
        model.addAttribute("prescriptions", prescriptions);
        return "prescriptions/prescriptionsList.html";
    }

    @GetMapping("/info/{id}")
    public String getPrescriptionDetails(@PathVariable Long id, Model model) {
        log.info("GET /prescriptions/info/{} – detalii rețetă", id);
        Prescription prescription = prescriptionService.getPrescription(id);
        model.addAttribute("prescription", prescription);
        model.addAttribute("medications", prescription.getMedications());
        return "prescriptions/prescriptionsInfo.html";
    }

    @GetMapping("/new")
    public String newPrescription(Model model) {
        log.info("GET /prescriptions/new – formular creare rețetă");
        model.addAttribute("prescription", new Prescription());
        model.addAttribute("appointments", appointmentService.getAllAppointments(null, null));
        return "prescriptions/prescriptionsNew.html";
    }

    @PostMapping
    public String createPrescription(@Valid @ModelAttribute Prescription prescription, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /prescriptions – eroare validare: {}", result.getAllErrors());
            return "prescriptions/prescriptionsNew.html";
        }
        try {
            log.info("POST /prescriptions – creare rețetă: {}", prescription);
            prescriptionService.createPrescription(prescription);
            return "redirect:/prescriptions";
        } catch (PrescriptionAlreadyExistingException ex) {
            log.warn("POST /prescriptions – rețetă deja există pentru programare: {}", ex.getMessage());
            result.rejectValue("appointment", "prescriptionAlreadyAssignedToAppt", ex.getMessage());
            return "prescriptions/prescriptionsNew.html";
        }
    }

    @GetMapping("/edit/{id}")
    public String editPrescriptionForm(@PathVariable Long id, Model model) {
        log.info("GET /prescriptions/edit/{} – formular editare rețetă", id);
        model.addAttribute("prescription", prescriptionService.getPrescription(id));
        return "prescriptions/prescriptionsEdit.html";
    }

    @PostMapping("/edit/{id}")
    public String editPrescription(@PathVariable Long id, @Valid @ModelAttribute Prescription prescription, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /prescriptions/edit/{} – eroare validare: {}", id, result.getAllErrors());
            return "prescriptions/prescriptionsEdit.html";
        }
        log.info("POST /prescriptions/edit/{} – actualizare rețetă: {}", id, prescription);
        prescription.setId(id);
        prescriptionService.updatePrescription(prescription);
        return "redirect:/prescriptions/info/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deletePrescription(@PathVariable Long id) {
        log.info("DELETE /prescriptions/{} – ștergere rețetă", id);
        prescriptionService.deletePrescription(id);
        return "redirect:/prescriptions";
    }
}
