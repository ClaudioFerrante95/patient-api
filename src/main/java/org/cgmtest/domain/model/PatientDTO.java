package org.cgmtest.domain.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PatientDTO {

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @NotBlank @Email
    public String email;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
    public String fiscalCode;

    @NotBlank
    @Pattern(regexp = "^[0-9]{10}$")
    public String phoneNumber;
}

