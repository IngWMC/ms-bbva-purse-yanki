package com.nttdata.bbva.purseyanki.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {
    private String id;
    private Date date;
    private EventOperation operation;
}
