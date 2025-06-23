package com.awbd.clinica.controller;

import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.service.ClinicService;
import com.awbd.clinica.service.DoctorService;
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
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;
    private final ClinicService clinicService;

    public DoctorController(DoctorService doctorService, ClinicService clinicService) {
        this.doctorService = doctorService;
        this.clinicService = clinicService;
    }

    @GetMapping
    public String getAllDoctors(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "id") String sortBy,
                                @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {
        log.info("GET /doctors – pagină={}, dimensiune={}, sortare={} {}", page, size, sortBy, sortOrder);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
        Page<Doctor> doctorPage = doctorService.getAllDoctorsPage(pageRequest);
        model.addAttribute("page", doctorPage);
        return "doctors/doctorsList.html";
    }

    @GetMapping("/info/{id}")
    public String getDoctor(@PathVariable Long id, Model model) {
        log.info("GET /doctors/info/{} – detalii doctor", id);
        Doctor doctor = doctorService.getDoctorEntity(id);
        model.addAttribute("doctor", doctor);
        return "doctors/doctorsInfo.html";
    }

    @GetMapping("/new")
    public String newDoctor(Model model) {
        log.info("GET /doctors/new – formular creare doctor");
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("clinics", clinicService.getAllClinicsList());
        return "doctors/doctorsNew.html";
    }

    @PostMapping
    public String createDoctor(@Valid @ModelAttribute Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /doctors – eroare validare: {}", result.getAllErrors());
            return "doctors/doctorsNew.html";
        }
        log.info("POST /doctors – creare doctor: {}", doctor);
        doctorService.createDoctor(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String editDoctorForm(@PathVariable Long id, Model model) {
        log.info("GET /doctors/edit/{} – formular editare doctor", id);
        model.addAttribute("doctor", doctorService.getDoctorEntity(id));
        return "doctors/doctorsEdit.html";
    }

    @PostMapping("/edit/{id}")
    public String editDoctor(@PathVariable Long id, @Valid @ModelAttribute Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("POST /doctors/edit/{} – eroare validare: {}", id, result.getAllErrors());
            return "doctors/doctorsEdit.html";
        }
        doctor.setId(id);
        log.info("POST /doctors/edit/{} – actualizare doctor: {}", id, doctor);
        doctorService.updateDoctor(doctor);
        return "redirect:/doctors/info/" + id;
    }

    @RequestMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable Long id) {
        log.info("DELETE /doctors/{} – ștergere doctor", id);
        doctorService.deleteDoctor(id);
        return "redirect:/doctors";
    }
}
