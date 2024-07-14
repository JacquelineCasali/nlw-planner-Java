package com.planner.activity;

import com.planner.participant.Participant;
import com.planner.trip.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity ,Long> {


    List<Activity> findByTripId(Long tripId);

}
