package com.planner.link;

import com.planner.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkRepository extends JpaRepository <Link, Long > {
    List<Link> findByTripId(Long tripId);
}
