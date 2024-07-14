package com.planner.trip;

import com.planner.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TripRepository  extends JpaRepository <Trip, Long> {
    Trip findByOwnerName(String ownerName);
    Trip  findByOwnerEmail( String ownerEmail);


}
