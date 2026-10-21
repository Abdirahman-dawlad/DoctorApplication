package com.doctorapp.DoctorApplication.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
    public class Patient {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(name = "phone_number")
        private String phoneNumber;

        private LocalDate dob;

        private String gender;

        private String address;

        @Column(name = "blood_group")
        private String bloodGroup;

        @Column(name = "medical_history", columnDefinition = "TEXT")
        private String medicalHistory;

        @OneToOne
        @JoinColumn(name = "user_id")
        private User user;

        @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
        private List<Appointment> appointments;
    }

