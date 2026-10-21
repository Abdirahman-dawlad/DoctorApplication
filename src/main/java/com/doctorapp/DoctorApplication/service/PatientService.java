package com.doctorapp.DoctorApplication.service;

import com.doctorapp.DoctorApplication.model.Patient;
import com.doctorapp.DoctorApplication.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {
        @Autowired
        private PatientRepository patientRepository;

        public List<Patient> getAllPatients() {
            return patientRepository.findAll();
        }

        public Optional<Patient> getPatientById(Long id) {
            return patientRepository.findById(id);
        }

        public Optional<Patient> getPatientByEmail(String email) {
            return patientRepository.findByEmail(email);
        }

        public Optional<Patient> getPatientByUserId(Long userId) {
            return patientRepository.findByUserId(userId);
        }

        public Patient registerPatient(Patient patient) {
            // Check if email already exists
            if (patientRepository.findByEmail(patient.getEmail()).isPresent()) {
                throw new RuntimeException("Email already registered");
            }

            // Save patient directly (without User entity if you don't have it)
            // or if you have User entity, you can set it here
            return patientRepository.save(patient);
        }

        public Patient updatePatient(Long id, Patient patientDetails) {
            Patient patient = patientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            patient.setName(patientDetails.getName());
            patient.setEmail(patientDetails.getEmail());
            patient.setPhoneNumber(patientDetails.getPhoneNumber());
            patient.setDob(patientDetails.getDob());
            patient.setGender(patientDetails.getGender());
            patient.setAddress(patientDetails.getAddress());
            patient.setBloodGroup(patientDetails.getBloodGroup());
            patient.setMedicalHistory(patientDetails.getMedicalHistory());

            return patientRepository.save(patient);
        }

        public void deletePatient(Long id) {
            patientRepository.deleteById(id);
        }

        public boolean existsByEmail(String email) {
            return patientRepository.findByEmail(email).isPresent();
        }

        public long countPatients() {
            return patientRepository.count();
        }
    }




