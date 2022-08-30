package com.nttdata.bbva.purseyanki.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class EventOperation {
	@Length(min = 9, max = 9, message = "El campo cellPhoneNumberTransfer debe tener 9 caracteres.")
	@NotEmpty(message = "El campo cellPhoneNumber es requerido.")
	private String cellPhoneNumber; // Número de celular - Origen
	private String openAccountId; // Número de la cuenta - Origen
	@Length(min = 9, max = 9, message = "El campo cellPhoneNumberTransfer debe tener 9 caracteres.")
	@NotEmpty(message = "El campo cellPhoneNumberTransfer es requerido.")
	private String cellPhoneNumberTransfer; // Número de celular - Destino
	private String openAccountIdTransfer; // Número de la cuenta - Destino
	private String operationType; // Transferencia: T
	@DecimalMin(value = "1.0", message = "El campo amount debe tener un valor mínimo de '1.0'.")
	@Digits(integer = 10, fraction = 3, message = "El campo amount tiene un formato no válido (#####.000).")
	@NotNull(message = "El campo amount es requerido.")
	private BigDecimal amount;
}