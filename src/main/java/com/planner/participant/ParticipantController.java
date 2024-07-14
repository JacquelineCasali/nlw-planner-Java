package com.planner.participant;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    //confirmaçao do participante
    @Autowired
    private ParticipantRepository participantRepository;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant>comfirmParticipant(@PathVariable Long id, @RequestBody ParticipantRequestPayload payload){
        Optional<Participant> participant=this.participantRepository.findById(id);
       //verificando se o participante existe
        if(participant.isPresent()){
Participant rawParticipant =participant.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());
            this.participantRepository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);

        }
        return ResponseEntity.notFound().build();
    }


}
