package com.doctorapp.DoctorApplication.repository;

import com.doctorapp.DoctorApplication.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

        List<Appointment> findByPatientId(Long patientId);

        List<Appointment> findByDoctorId(Long doctorId);

        @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.appointmentDateTime = :dateTime")
        List<Appointment> findAppointmentsByDoctorAndDateTime(@Param("doctorId") Long doctorId,
                                                              @Param("dateTime") LocalDateTime dateTime);

        @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
        List<Appointment> findAppointmentsByPatientAndStatus(@Param("patientId") Long patientId,
                                                             @Param("status") String status);

        long countByDoctorIdAndStatus(Long doctorId, String status);
    }

