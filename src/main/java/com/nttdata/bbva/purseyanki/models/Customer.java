package com.nttdata.bbva.purseyanki.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
	private String id;
	private String fullName;
	private String customerTypeId;
	private String identificationDocument;
	private String emailAddress;
}
