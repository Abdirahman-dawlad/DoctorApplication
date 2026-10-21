package com.doctorapp.DoctorApplication.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "doctor_id", nullable = false)
        private Doctor doctor;

        @ManyToOne
        @JoinColumn(name = "patient_id", nullable = false)
        private Patient patient;

        @Column(name = "appointment_date", nullable = false)
        private LocalDateTime appointmentDateTime;

        private String status; // SCHEDULED, CONFIRMED, COMPLETED, CANCELLED

        private String symptoms;

        @Column(columnDefinition = "TEXT")
        private String notes;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Column(name = "meeting_link")
        private String meetingLink;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
            updatedAt = LocalDateTime.now();
            if (status == null) {
                status = "SCHEDULED";
            }
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = LocalDateTime.now();
        }
    }

