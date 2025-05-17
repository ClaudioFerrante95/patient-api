package org.cgmtest.domain.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Patient extends PanacheEntity {

    @NotBlank
    public String firstName;

    @NotBlank
    public String lastName;

    @Email
    @NotBlank
    public String email;

    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$", message = "Codice fiscale non valido")
    @Column(unique = true)
    public String fiscalCode;

    @Pattern(regexp = "^[0-9]{10}$", message = "Numero di telefono non valido")
    public String phoneNumber;
}
