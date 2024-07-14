package com.planner.trip;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TripRequestPayload(

        @NotBlank(message = "Destino é obrigatório")
        String destination,
      String  starts_at ,
     String   ends_at ,
     List<String> emails_to_invite,

        @NotBlank(message = "Nome é obrigatório")
        String  owner_name ,
        @NotBlank(message = "Email é obrigatório")
     String  owner_email
) {


}
