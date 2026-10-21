package com.doctorapp.DoctorApplication.service;

import com.doctorapp.DoctorApplication.model.Appointment;
import com.doctorapp.DoctorApplication.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {

        @Autowired
        private AppointmentRepository appointmentRepository;

        public Appointment bookAppointment(Appointment appointment) {
            // Generate meeting link for virtual consultation
            appointment.setMeetingLink(generateMeetingLink());
            return appointmentRepository.save(appointment);
        }

        public List<Appointment> getAppointmentsByPatient(Long patientId) {
            return appointmentRepository.findByPatientId(patientId);
        }

        public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
            return appointmentRepository.findByDoctorId(doctorId);
        }

        public Optional<Appointment> getAppointmentById(Long id) {
            return appointmentRepository.findById(id);
        }

        public Appointment updateAppointmentStatus(Long id, String status) {
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        }

        public boolean isDoctorAvailable(Long doctorId, LocalDateTime dateTime) {
            List<Appointment> existingAppointments =
                    appointmentRepository.findAppointmentsByDoctorAndDateTime(doctorId, dateTime);
            return existingAppointments.isEmpty();
        }

        public void cancelAppointment(Long id) {
            Appointment appointment = appointmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
            appointment.setStatus("CANCELLED");
            appointmentRepository.save(appointment);
        }

        private String generateMeetingLink() {
            return "https://meet.doctorapp.com/" + UUID.randomUUID().toString().substring(0, 8);
        }
    }

