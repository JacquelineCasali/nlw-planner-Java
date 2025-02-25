package com.planner.link;

import com.planner.activity.ActivityData;
import com.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkService {
    @Autowired
private LinkRepository linkRepository;

    public LinkResponse registerLink(LinkRequestPayload payload, Trip trip){

        Link newLink=new Link(payload.title(), payload.url(), trip);
        this.linkRepository.save(newLink);
        return new LinkResponse(newLink.getId(),newLink.getTitle(),newLink.getUrl());
    }



    public List<LinkData> getAllLinkFromTrip(Long tripId) {
        return this.linkRepository.findByTripId(tripId).stream().map(link -> new LinkData(
                link.getId(),link.getTitle(),link.getUrl())).toList();


    }

}
