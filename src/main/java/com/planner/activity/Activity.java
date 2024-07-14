package com.planner.activity;

import com.planner.trip.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "activities")
// get e set
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title é obrigatório")
    private String title;

    @Column(name = "occurs_at" , nullable = false)
    private LocalDateTime occursAt ;


    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Activity(String title,String occursAt,Trip trip){
        this.title=title;
        this.occursAt = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);
    this.trip=trip;
    }
}
