package com.awbd.clinica.repository;

import com.awbd.clinica.domain.Doctor;
import com.awbd.clinica.domain.Speciality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    String FILTER_DOCTORS_ON_SPECIALITY = "select d from Doctor d where d.speciality like UPPER(?1)";

    @Query(FILTER_DOCTORS_ON_SPECIALITY)
    Page<Doctor> findBySpeciality(String specialityFilter, Pageable pageable);

    Page<Doctor> findAll(Pageable page);
    List<Doctor> findByClinicId(Long clinicId);
    List<Doctor> findBySpeciality(Speciality speciality);


}
