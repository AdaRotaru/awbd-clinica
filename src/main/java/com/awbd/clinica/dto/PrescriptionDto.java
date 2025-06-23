package com.awbd.clinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrescriptionDto {

    @NotNull(message = "Appointment ID must be provided.")
    private Long appointmentId;

    @NotBlank(message = "Comments must be provided.")
    private String comments;

    private List<Long> medicationIds;
}
