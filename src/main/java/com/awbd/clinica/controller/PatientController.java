package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Appointment;
import com.awbd.clinica.domain.Patient;
import com.awbd.clinica.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/new")
    public String newPatient(Model model) {
        log.info("GET /patients – listare pacienți");
        model.addAttribute("patient", new Patient());
        return "patients/patientsNew.html";
    }

    @PostMapping
    public String createPatient(@Valid @ModelAttribute Patient patient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /patients – eroare validare: {}", bindingResult.getAllErrors());
            return "patients/patientsNew.html";
        }
        patientService.createPatient(patient);
        return "redirect:/patients";
    }

    @GetMapping("/info/{id}")
    public String getPatient(@PathVariable Long id, Model model) {
        log.info("GET /patients/info/{} – detalii pacient", id);
        Patient patient = patientService.getPatientEntity(id);
        model.addAttribute("patient", patient);
        return "patients/patientsInfo.html";
    }

    @GetMapping("/info/{id}/upcomingAppointments")
    public String getPatientUpcomingAppointments(@PathVariable Long id, Model model) {
        log.info("GET /patients/info/{}/upcomingAppointments – listare programări viitoare", id);
        List<Appointment> appointments = patientService.getPatientUpcomingAppointments(id);
        model.addAttribute("appointments", appointments);
        return "patients/patientsFutureAppointments.html";
    }

    @GetMapping
    public String getAllPatients(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
        Page<Patient> patientPage = patientService.getAllPatients(pageRequest);
        model.addAttribute("page", patientPage);

        return "patients/patientsList.html";
    }

    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.getPatient(id));
        return "patients/patientsEdit.html";
    }

    @PostMapping("/edit/{id}")
    public String editPatient(@PathVariable Long id, @Valid @ModelAttribute Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            return "patients/patientsEdit.html";
        }
        patient.setId(id);
        patientService.updatePatient(patient);
        return "redirect:/patients/info/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }
}
