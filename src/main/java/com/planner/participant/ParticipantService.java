package com.planner.participant;

import com.planner.exception.ValidacaoException;
import com.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

//participante do evento
    public void registerParticipantsToEvent(List<String>participantsToInvite, Trip trip){
//lista de participante pegando os emails recebido participantsToInvite e criando um novo participante
        List<Participant> participants= participantsToInvite.stream().map(email-> new Participant(email,trip)).toList();
        //saveAll salva todos
this.participantRepository.saveAll(participants);
        System.out.println(participants.get(0).getId());
    }

    public ParticipantCreateResponse registerParticipantToEvent(String email,Trip trip ){
      Participant newParticipant= new Participant(email,trip);
        this.participantRepository.save(newParticipant);
return new ParticipantCreateResponse(newParticipant.getId(), newParticipant.getEmail());
    };

    public void triggerConfirmationEmailToParticipants(Long tripId){};

    public void triggerConfirmationEmailToParticipant(String email){};

    public List<ParticipantData> getAllParticipantsFromEvent(Long tripId) {
   return this.participantRepository.findByTripId(tripId).stream().map(participant -> new ParticipantData(
           participant.getId(),participant.getName(),participant.getEmail(),participant.getIsConfirmed()
   )).toList();


    }

}
