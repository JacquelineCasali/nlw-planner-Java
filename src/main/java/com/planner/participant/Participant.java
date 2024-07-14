package com.planner.participant;

import com.planner.trip.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participants")
// get e set
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false)

    @Email
    private String email;


    //    uma viagem podem ter varios participantes
//    um participante pertece a uma viagem
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    //    Trip recebe a viagem
    public Participant(String email, Trip trip) {
        this.email = email;
        this.trip = trip;
        this.isConfirmed = false;
        this.name = "";
    }
}
