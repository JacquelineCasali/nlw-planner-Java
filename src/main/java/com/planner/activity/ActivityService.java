package com.planner.activity;

import java.util.List;
import com.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;



@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
public  ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip){

    Activity newActivity=new Activity(payload.title(), payload.occurs_at(), trip);
this.activityRepository.save(newActivity);
return new ActivityResponse(newActivity.getId(),newActivity.getTitle());
}



    public List<ActivityData> getAllActivityFromId(Long tripId) {
        return this.activityRepository.findByTripId(tripId).stream().map(activity -> new ActivityData(
                activity.getId(),activity.getTitle(),activity.getOccursAt())).toList();


    }
}
