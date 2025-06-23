package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Medication;
import com.awbd.clinica.service.MedicationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public String getAllMedications(Model model, @RequestParam(required = false) String brand) {
        log.info("GET /medications – listare medicamente, brand={}", brand);
        List<Medication> medications = medicationService.getAllMedications(brand);
        model.addAttribute("medications", medications);
        return "medications/medicationsList.html";
    }

    @GetMapping("/info/{id}")
    public String getMedication(@PathVariable Long id, Model model) {
        log.info("GET /medications/info/{} – detalii medicament", id);
        Medication medication = medicationService.getMedication(id);
        model.addAttribute("medication", medication);
        return "medications/medicationsInfo.html";
    }

    @GetMapping("/new")
    public String newMedication(Model model) {
        log.info("GET /medications/new – formular creare medicament");
        model.addAttribute("medication", new Medication());
        return "medications/medicationsNew.html";
    }

    @PostMapping
    public String createMedication(@Valid @ModelAttribute Medication medication, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("POST /medications – eroare validare: {}", bindingResult.getAllErrors());
            return "medications/medicationsNew.html";
        }
        log.info("POST /medications – creare medicament: {}", medication);
        medicationService.createMedication(medication);
        return "redirect:/medications";
    }

    @GetMapping("/edit/{id}")
    public String editMedicationForm(@PathVariable Long id, Model model) {
        log.info("GET /medications/edit/{} – formular editare medicament", id);
        model.addAttribute("medication", medicationService.getMedication(id));
        return "medications/medicationsEdit.html";
    }

    @PostMapping("/edit/{id}")
    public String editMedication(@PathVariable Long id, @Valid @ModelAttribute Medication medication, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /medications/edit/{} – eroare validare: {}", id, result.getAllErrors());
            return "medications/medicationsEdit.html";
        }
        medication.setId(id);
        log.info("POST /medications/edit/{} – actualizare medicament: {}", id, medication);
        medicationService.updateMedication(medication);
        return "redirect:/medications/info/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deleteMedication(@PathVariable Long id) {
        log.info("DELETE /medications/{} – ștergere medicament", id);
        medicationService.deleteMedication(id);
        return "redirect:/medications";
    }
}
