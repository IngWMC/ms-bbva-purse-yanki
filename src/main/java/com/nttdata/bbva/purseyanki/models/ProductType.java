package com.nttdata.bbva.purseyanki.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {
	private String id;
	private String name;
	private String shortName;
}
