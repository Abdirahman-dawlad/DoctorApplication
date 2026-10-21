package com.doctorapp.DoctorApplication.controller;


import com.doctorapp.DoctorApplication.model.Appointment;
import com.doctorapp.DoctorApplication.model.Doctor;
import com.doctorapp.DoctorApplication.model.Patient;
import com.doctorapp.DoctorApplication.service.AppointmentService;
import com.doctorapp.DoctorApplication.service.DoctorService;
import com.doctorapp.DoctorApplication.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

        @Autowired
        private AppointmentService appointmentService;

        @Autowired
        private DoctorService doctorService;

        @Autowired
        private PatientService patientService;

        @GetMapping("/book")
        public String showBookingForm(@RequestParam Long doctorId, Model model) {
            try {
                Doctor doctor = doctorService.getDoctorById(doctorId)
                        .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

                // Get patient with ID 1 (Michael Brown from your database)
                Patient patient = patientService.getPatientById(1L)
                        .orElseThrow(() -> new RuntimeException("Patient not found with ID: 1"));

                Appointment appointment = new Appointment();
                appointment.setDoctor(doctor);
                appointment.setPatient(patient);

                model.addAttribute("appointment", appointment);
                model.addAttribute("doctor", doctor);
                model.addAttribute("patient", patient);
                return "appointments/book";

            } catch (Exception e) {
                model.addAttribute("error", "Error: " + e.getMessage());
                return "redirect:/doctors";
            }
        }

        @PostMapping("/book")
        public String bookAppointment(@ModelAttribute Appointment appointment,
                                      @RequestParam Long patientId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                      RedirectAttributes redirectAttributes) {

            try {
                Patient patient = patientService.getPatientById(patientId)
                        .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

                appointment.setPatient(patient);
                appointment.setAppointmentDateTime(dateTime);

                // Check if doctor is available
                if (!appointmentService.isDoctorAvailable(appointment.getDoctor().getId(), dateTime)) {
                    redirectAttributes.addFlashAttribute("error", "Doctor is not available at this time");
                    return "redirect:/appointments/book?doctorId=" + appointment.getDoctor().getId();
                }

                Appointment booked = appointmentService.bookAppointment(appointment);
                redirectAttributes.addFlashAttribute("success", "Appointment booked successfully!");
                return "redirect:/appointments/" + booked.getId();

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error booking appointment: " + e.getMessage());
                return "redirect:/appointments/book?doctorId=" + appointment.getDoctor().getId();
            }
        }

        @GetMapping("/{id}")
        public String viewAppointment(@PathVariable Long id, Model model) {
            try {
                Appointment appointment = appointmentService.getAppointmentById(id)
                        .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));
                model.addAttribute("appointment", appointment);
                return "appointments/detail";
            } catch (Exception e) {
                model.addAttribute("error", "Appointment not found");
                return "redirect:/appointments/my-appointments?patientId=1";
            }
        }

        @GetMapping("/my-appointments")
        public String viewMyAppointments(@RequestParam(required = false) Long patientId, Model model) {
            // Use patient ID 1 (Michael Brown) if none provided
            if (patientId == null) {
                patientId = 1L;
            }

            try {
                // Check if patient exists
                Optional<Patient> patientOpt = patientService.getPatientById(patientId);
                if (patientOpt.isEmpty()) {
                    model.addAttribute("error", "Patient not found with ID: " + patientId);
                    model.addAttribute("upcomingAppointments", new ArrayList<>());
                    model.addAttribute("pastAppointments", new ArrayList<>());
                    model.addAttribute("cancelledAppointments", new ArrayList<>());
                    return "appointments/my-appointments";
                }

                // Get all appointments for the patient
                List<Appointment> allAppointments = appointmentService.getAppointmentsByPatient(patientId);
                LocalDateTime now = LocalDateTime.now();

                // Separate into categories
                List<Appointment> upcoming = new ArrayList<>();
                List<Appointment> past = new ArrayList<>();
                List<Appointment> cancelled = new ArrayList<>();

                for (Appointment app : allAppointments) {
                    if (app.getStatus().equals("CANCELLED")) {
                        cancelled.add(app);
                    } else if (app.getAppointmentDateTime().isAfter(now)) {
                        upcoming.add(app);
                    } else {
                        past.add(app);
                    }
                }

                // Sort: upcoming by date ascending, past by date descending
                upcoming.sort((a1, a2) -> a1.getAppointmentDateTime().compareTo(a2.getAppointmentDateTime()));
                past.sort((a1, a2) -> a2.getAppointmentDateTime().compareTo(a1.getAppointmentDateTime()));
                cancelled.sort((a1, a2) -> a2.getAppointmentDateTime().compareTo(a1.getAppointmentDateTime()));

                model.addAttribute("patient", patientOpt.get());
                model.addAttribute("upcomingAppointments", upcoming);
                model.addAttribute("pastAppointments", past);
                model.addAttribute("cancelledAppointments", cancelled);
                model.addAttribute("totalAppointments", allAppointments.size());

            } catch (Exception e) {
                model.addAttribute("error", "Error loading appointments: " + e.getMessage());
                model.addAttribute("upcomingAppointments", new ArrayList<>());
                model.addAttribute("pastAppointments", new ArrayList<>());
                model.addAttribute("cancelledAppointments", new ArrayList<>());
            }

            return "appointments/my-appointments";
        }

        @PostMapping("/{id}/cancel")
        public String cancelAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
            try {
                appointmentService.cancelAppointment(id);
                redirectAttributes.addFlashAttribute("success", "Appointment cancelled successfully");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Error cancelling appointment: " + e.getMessage());
            }
            return "redirect:/appointments/" + id;
        }
    }

