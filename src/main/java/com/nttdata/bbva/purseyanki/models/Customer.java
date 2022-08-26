package com.nttdata.bbva.purseyanki.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @NotEmpty(message = "El campo identificationDocument es requerido.")
    private String identificationDocument;
    @NotEmpty(message = "El campo cellPhoneNumber es requerido.")
    private String cellPhoneNumber;
    @NotEmpty(message = "El campo imei es requerido.")
    private String imei;
    @NotEmpty(message = "El campo emailAddress es requerido.")
    @Email(message = "El campo emailAddress tiene un formato no válido.")
    private String emailAddress;
}
