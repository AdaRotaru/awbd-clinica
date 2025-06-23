package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Clinic;
import com.awbd.clinica.service.ClinicService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping
    public String getAllClinics(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy,
                                @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
        Page<Clinic> clinicPage = clinicService.getAllClinics(pageRequest);
        model.addAttribute("page", clinicPage);
        return "clinics/clinicsList.html";
    }

    @GetMapping("/info/{id}")
    public String getClinic(@PathVariable Long id, Model model) {
        log.info("GET /clinics/info/{} – detalii clinică", id);
        Clinic clinic = clinicService.getClinic(id);
        model.addAttribute("clinic", clinic);
        return "clinics/clinicsInfo.html";
    }

    @GetMapping("/new")
    public String newClinic(Model model) {
        log.info("GET /clinics/new – formular creare clinică");
        model.addAttribute("clinic", new Clinic());
        return "clinics/clinicsNew.html";
    }

    @PostMapping
    public String createClinic(@Valid @ModelAttribute Clinic clinic, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /clinics – eroare validare: {}", result.getAllErrors());
            return "clinics/clinicsNew.html";
        }
        log.info("POST /clinics – creare clinică: {}", clinic);
        clinicService.createClinic(clinic);
        return "redirect:/clinics";
    }

    @GetMapping("/edit/{id}")
    public String editClinicForm(@PathVariable Long id, Model model) {
        log.info("GET /clinics/edit/{} – formular editare clinică", id);
        model.addAttribute("clinic", clinicService.getClinic(id));
        return "clinics/clinicsEdit.html";
    }

    @PostMapping("/edit/{id}")
    public String editClinic(@PathVariable Long id, @Valid @ModelAttribute Clinic clinic, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /clinics/edit/{} – eroare validare: {}", id, result.getAllErrors());
            return "clinics/clinicsEdit.html";
        }
        log.info("POST /clinics/edit/{} – actualizare clinică: {}", id, clinic);
        clinic.setId(id);
        clinicService.updateClinic(clinic);
        return "redirect:/clinics/info/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deleteClinic(@PathVariable Long id) {
        log.info("DELETE /clinics/{} – ștergere clinică", id);
        clinicService.deleteClinic(id);
        return "redirect:/clinics";
    }
}
