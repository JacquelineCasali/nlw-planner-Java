package com.planner.link;

import com.planner.trip.Trip;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "links")
// get e set
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @URL
    private String url ;


    @Column(nullable = false)
    @NotBlank(message = "Title é obrigatório")
    private String title;

     @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Link(String title,String url,Trip trip){
        this.title=title;
        this.url = url;
        this.trip=trip;
    }
}
