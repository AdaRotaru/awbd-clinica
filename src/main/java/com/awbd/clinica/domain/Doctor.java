package com.awbd.clinica.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_seq")
    @SequenceGenerator(name = "doctor_seq", sequenceName = "DOCTOR_SEQ", allocationSize = 1)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Speciality speciality;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    public Doctor(String firstName, String lastName, Speciality speciality, Clinic clinic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
        this.clinic = clinic;
    }

    public Doctor(String firstName, String lastName, Speciality speciality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.speciality = speciality;
    }
}
