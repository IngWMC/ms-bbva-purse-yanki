package com.nttdata.bbva.purseyanki.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotEmpty(message = "El campo identificationDocument es requerido.")
    private String identificationDocument;
    @NotEmpty(message = "El campo cellPhoneNumber es requerido.")
    private String cellPhoneNumber;
    @NotEmpty(message = "El campo imei es requerido.")
    private String imei;
    @NotEmpty(message = "El campo emailAddress es requerido.")
    @Email(message = "El campo emailAddress tiene un formato no válido.")
    private String emailAddress;
    @NotEmpty(message = "El campo bankAccountNumber es requerido.")
    private String bankAccountNumber;
}
