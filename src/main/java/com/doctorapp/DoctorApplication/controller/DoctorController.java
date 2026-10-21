package com.doctorapp.DoctorApplication.controller;


import org.springframework.ui.Model;
import com.doctorapp.DoctorApplication.model.Doctor;
import com.doctorapp.DoctorApplication.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/doctors")
public class DoctorController {


        @Autowired
        private DoctorService doctorService;

        @GetMapping
        public String listDoctors(Model model) {
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("specializations", doctorService.getAllSpecializations());
            return "doctors/list";
        }

        @GetMapping("/{id}")
        public String viewDoctor(@PathVariable Long id, Model model) {
            Doctor doctor = doctorService.getDoctorById(id)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            model.addAttribute("doctor", doctor);
            return "doctors/detail";
        }

        @GetMapping("/search")
        public String searchDoctors(@RequestParam String keyword, Model model) {
            model.addAttribute("doctors", doctorService.searchDoctors(keyword));
            model.addAttribute("keyword", keyword);
            return "doctors/list";
        }

        @GetMapping("/specialization/{specialization}")
        public String filterBySpecialization(@PathVariable String specialization, Model model) {
            model.addAttribute("doctors", doctorService.getDoctorsBySpecialization(specialization));
            model.addAttribute("selectedSpecialization", specialization);
            return "doctors/list";
        }
    }

