package com.doctorapp.DoctorApplication.service;

import com.doctorapp.DoctorApplication.model.Doctor;
import com.doctorapp.DoctorApplication.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class DoctorService {


        @Autowired
        private DoctorRepository doctorRepository;

        public List<Doctor> getAllDoctors() {
            return doctorRepository.findAll();
        }

        public Optional<Doctor> getDoctorById(Long id) {
            return doctorRepository.findById(id);
        }

        public Doctor saveDoctor(Doctor doctor) {
            return doctorRepository.save(doctor);
        }

        public void deleteDoctor(Long id) {
            doctorRepository.deleteById(id);
        }

        public List<Doctor> getDoctorsBySpecialization(String specialization) {
            return doctorRepository.findBySpecialization(specialization);
        }

        public List<Doctor> getAvailableDoctors() {
            return doctorRepository.findByIsAvailableTrue();
        }

        public List<Doctor> searchDoctors(String keyword) {
            return doctorRepository.searchDoctors(keyword);
        }

        public List<String> getAllSpecializations() {
            return doctorRepository.findAllSpecializations();
        }
    }

