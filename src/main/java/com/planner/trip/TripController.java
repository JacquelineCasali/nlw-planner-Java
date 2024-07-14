package com.planner.trip;


import com.planner.activity.*;
import com.planner.link.LinkData;
import com.planner.link.LinkRequestPayload;
import com.planner.link.LinkResponse;
import com.planner.participant.*;
import com.planner.link.LinkService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;
    @Autowired
    private TripRepository tripRepository;

    @GetMapping
    public List<Trip> listar() {
        return tripRepository.findAll();

    }

    //com validação
    @PostMapping
    public ResponseEntity creteTrips(@Valid @RequestBody TripRequestPayload payload) {
        var name = this.tripRepository.findByOwnerName(payload.owner_name());
        var email = this.tripRepository.findByOwnerEmail(payload.owner_email());

        if (name != null) {
            // Mensagem de erro
            // Status Code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome já cadastrado");
        } else if (email != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado");
//            throw new ValidacaoException("E-mail já cadastrado");
        }


        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME)) || currentDate.isAfter(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME))) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início / data de término deve ser maior do que a data atual");
//            throw new ValidacaoException("A data de início / data de término deve ser maior do que a data atual");
        }

        if (LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME).isAfter(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME))) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor do que a data de término");
            //   throw new ValidacaoException("A data de início deve ser menor do que a data de término");
        }


        Trip newTrip = new Trip(payload);
        this.tripRepository.save(newTrip);
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
        return ResponseEntity.status(HttpStatus.OK).body(newTrip);

    }

    //detalhar Viagem
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable Long id) {
//        Optional pode ou nao encontrar o id
        Optional<Trip> trip = this.tripRepository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //editar viagem
    @PutMapping("/{id}")
    public ResponseEntity updatedTrip(@Valid @RequestBody TripRequestPayload payload, @PathVariable long id) {
        //verificando se a viagem existe
        Optional<Trip> trip = this.tripRepository.findById(id);
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME)) || currentDate.isAfter(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME))) {
//            throw new ValidacaoException("A data de início / data de término deve ser maior do que a data atual");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início / data de término deve ser maior do que a data atual");

        }
        if (LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME).isAfter(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor do que a data de término");
//            throw new ValidacaoException("A data de início deve ser menor do que a data de término");
        }
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());
            rawTrip.setOwnerName(payload.owner_name());
            rawTrip.setOwnerEmail(payload.owner_email());
            this.tripRepository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);

        }
//   throw new ValidacaoException("Id da Viagem não encontrado");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id da Viagem não encontrado");
//        return ResponseEntity.notFound().build();

    }

    //confirmação da viagem
    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable Long id) {
//        Optional pode ou nao encontrar o id
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);
            this.tripRepository.save(rawTrip);
            //           enviando email de confirmação da viagem
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }


    //convidar participante
    @PostMapping("/{id}/invite")
//    <> retorna
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable Long id, @RequestBody ParticipantRequestPayload payload) {
//        Optional pode ou nao encontrar o id
        Optional<Trip> trip = this.tripRepository.findById(id);
//verificando se a viagem existe

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            //cria um novo participante
            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(), rawTrip);
            //verificando se o evento foi confirmado ou nao

            if (rawTrip.getIsConfirmed())
                this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();

    }

    //Lista de  participantes
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> todosParticipants(@PathVariable Long id) {
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvent(id);
        return ResponseEntity.ok(participantList);
    }


    //cadastrar atividade
    @PostMapping("/{id}/activities")
//    <> retorna
    public ResponseEntity registerActivity(@Valid @PathVariable Long id, @RequestBody ActivityRequestPayload payload) {
//        Optional pode ou nao encontrar o id
        Optional<Trip> trip = this.tripRepository.findById(id);
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME))) {
//            throw new ValidacaoException("A data de início / data de término deve ser maior do que a data atual");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data da atividade tem que ser maior que a data atual ");

        }
        //verificando se a viagem existe
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);
            return ResponseEntity.ok(activityResponse);
        }
        return ResponseEntity.notFound().build();

    }

    //Lista de  atividade
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> todasActivity(@PathVariable Long id) {
        List<ActivityData> activityDataList = this.activityService.getAllActivityFromId(id);
        return ResponseEntity.ok(activityDataList);
    }

    //Criar Link
    @PostMapping("/{id}/links")
//    <> retorna
    public ResponseEntity registerLink(@Valid @PathVariable Long id, @RequestBody LinkRequestPayload payload) {
//        Optional pode ou nao encontrar o id
        Optional<Trip> trip = this.tripRepository.findById(id);
        //verificando se a viagem existe
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);
            return ResponseEntity.ok(linkResponse);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique os campos");

    }

    //Lista de  links
    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> todasLinks(@Valid @PathVariable Long id) {
        List<LinkData> linkDataList = this.linkService.getAllLinkFromTrip(id);
        return ResponseEntity.ok(linkDataList);
    }

}
