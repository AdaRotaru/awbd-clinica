package com.awbd.clinica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotNull(message = "Date of birth is required.")
    private LocalDate dateOfBirth;


    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^(\\d{3}[- .]?){2}\\d{4}$|^\\(\\d{3}\\) \\d{3}-\\d{4}$",
            message = "Phone number format is invalid."
    )
    private String phone;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;
}
