package com.doctorapp.DoctorApplication.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String specialization;

        private String qualification;

        private Integer experience;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(name = "consultation_fee")
        private Double consultationFee;

        @Column(name = "available_from")
        private String availableFrom;

        @Column(name = "available_to")
        private String availableTo;

        @Column(name = "is_available")
        private Boolean isAvailable = true;

        @Column(name = "profile_picture")
        private String profilePicture;

        @OneToOne
        @JoinColumn(name = "user_id")
        private User user;

        @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
        private List<Appointment> appointments;

        private String email;
        private String phone;
    }

