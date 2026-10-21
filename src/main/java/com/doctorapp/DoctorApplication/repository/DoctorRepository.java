package com.doctorapp.DoctorApplication.repository;

import com.doctorapp.DoctorApplication.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByIsAvailableTrue();

    @Query("SELECT d FROM Doctor d WHERE d.specialization LIKE %:keyword% OR d.name LIKE %:keyword%")
    List<Doctor> searchDoctors(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT d.specialization FROM Doctor d")
    List<String> findAllSpecializations();
}

