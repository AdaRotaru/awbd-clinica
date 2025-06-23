package com.awbd.clinica.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "med_seq")
    @SequenceGenerator(name = "med_seq", sequenceName = "MEDICATION_SEQ", allocationSize = 1)
    private Long id;
    private String name;
    private String brand;
    @JsonIgnore
    @ManyToMany(mappedBy = "medications")
    private List<Prescription> prescriptions;

    public Medication(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }
}