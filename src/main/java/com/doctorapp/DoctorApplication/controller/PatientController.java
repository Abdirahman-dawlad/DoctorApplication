package com.doctorapp.DoctorApplication.controller;

import com.doctorapp.DoctorApplication.model.Patient;
import com.doctorapp.DoctorApplication.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patients")
public class PatientController {

        @Autowired
        private PatientService patientService;

        @GetMapping("/register")
        public String showRegistrationForm(Model model) {
            model.addAttribute("patient", new Patient());
            return "patients/register";
        }

        @PostMapping("/register")
        public String registerPatient(@Valid @ModelAttribute Patient patient,
                                      BindingResult result,
                                      Model model) {
            if (result.hasErrors()) {
                return "patients/register";
            }

            try {
                patientService.registerPatient(patient);
                return "redirect:/patients/profile?success";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "patients/register";
            }
        }

        @GetMapping("/profile")
        public String viewProfile(@RequestParam Long id, Model model) {
            Patient patient = patientService.getPatientById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            model.addAttribute("patient", patient);
            return "patients/profile";
        }
    }

