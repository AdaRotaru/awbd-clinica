package com.awbd.clinica.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicationDto {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Brand is required.")
    private String brand;
}
