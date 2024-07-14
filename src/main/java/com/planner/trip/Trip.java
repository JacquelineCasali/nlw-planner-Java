package com.planner.trip;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// entidade
@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    // colunas da tabela
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //nao pode ser nula e nem vazia
    @NotBlank(message = "Destino é obrigatório")
    @Column(nullable = false)
    private String destination;
    //     inicio do evento
//    nome do evento na tabela
    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;
//    inicio do evento

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;
//    confirmado ou nao

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    //  nome do proprietario
    @NotBlank(message = "Nome é obrigatório")

    @Column(name = "owner_name", nullable = false, unique = true)
    private String ownerName;


    //  email do proprietario
    @NotBlank(message = "E-mail é obrigatório")
    @Email
    @Column(name = "owner_email", nullable = false, unique = true)
    private String ownerEmail;

    public Trip(TripRequestPayload data) {
        this.destination = data.destination();
        this.isConfirmed = false;
        this.ownerEmail = data.owner_email();
        this.ownerName = data.owner_name();
//        data
        this.startsAt = LocalDateTime.parse(data.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = LocalDateTime.parse(data.ends_at(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
