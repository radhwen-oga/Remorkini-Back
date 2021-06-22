package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.DemandeRemorquage;
import com.onegateafrica.Entities.Location;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Payloads.request.DemandeRemorquageAccepteDto;
import com.onegateafrica.Payloads.request.DemandeRemorquageDto;
import com.onegateafrica.Payloads.response.VerificationChangementRemorqeurResponse;
import com.onegateafrica.Repositories.DemandeRemorquageRepository;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.DemandeRemorquageService;
import com.onegateafrica.Service.RemorqueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/demandeRemorquage")

public class DemandeRemorquageController {

  private final RemorqueurService remorqueurService ;
  private final DemandeRemorquageRepository demandeRemorquageRepository ;
  private final DemandeRemorquageService demandeRemorquageService ;
  private final ConsommateurService consommateurService ;

  @Autowired
  public DemandeRemorquageController(RemorqueurService remorqueurService, DemandeRemorquageRepository demandeRemorquageRepository, DemandeRemorquageService demandeRemorquageService, ConsommateurService consommateurService) {
    this.remorqueurService = remorqueurService;
    this.demandeRemorquageRepository = demandeRemorquageRepository;
    this.demandeRemorquageService = demandeRemorquageService;
    this.consommateurService = consommateurService;
  }

  @PostMapping("/addDemande")
  //@PreAuthorize("hasRole('CONSOMMATEUR')")
  public ResponseEntity< Object > addDemandeRemorquage(@RequestBody DemandeRemorquageDto demandeRemorquageDto){

   if(demandeRemorquageDto != null) {
     try {
         Optional<Consommateur> consommateur = consommateurService.getConsommateur(demandeRemorquageDto.getIdConsommateur());

         DemandeRemorquage demandeRemorquage = new DemandeRemorquage();
         Consommateur entity = consommateur.get();

         demandeRemorquage.setConsommateur(entity);
         //demandeRemorquage.setDescription(demandeRemorquageDto.getDescription());
         demandeRemorquage.setMarqueVoiture(demandeRemorquageDto.getMarqueVoiture());
         demandeRemorquage.setNbrePersonnes(demandeRemorquageDto.getNbrePersonnes());
         demandeRemorquage.setTypePanne(demandeRemorquageDto.getTypePanne());
         Instant now = Instant.now();
         demandeRemorquage.setDateCreation(Timestamp.from(now));

         Location depart = new Location(demandeRemorquageDto.getDepartLattitude(), demandeRemorquageDto.getDepartLongitude());
         // depart.setDemandeRemorquageDepart(demandeRemorquage);
         Location destination = new Location(demandeRemorquageDto.getDestinationLattitude(), demandeRemorquageDto.getDestinationLongitude());
//       destination.setDemandeRemorquageDestination(demandeRemorquage);

         demandeRemorquage.setDepartRemorquage(depart);
         demandeRemorquage.setDestinationRemorquage(destination);

         List<DemandeRemorquage> listeDemandeRemorquage = new ArrayList<>();
         listeDemandeRemorquage.add(demandeRemorquage);

         entity.setListeDemandesRemorquage(listeDemandeRemorquage);

         demandeRemorquageRepository.save(demandeRemorquage);
         //consommateurService.saveOrUpdateConsommateur(entity);
         return ResponseEntity.status(HttpStatus.OK).body(demandeRemorquage);
     }
     catch (Exception e) {
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur");
     }
   }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur ");

  }

  @GetMapping("/verifierPossibiliteChangementRemorqueur/{idDemande}")
  public ResponseEntity<Object> VerifierPermissionChangementRemorqueur(@PathVariable  Long idDemande) {
    if(idDemande != null) {
      try {
        VerificationChangementRemorqeurResponse resVerif = demandeRemorquageService.permettreChangementRemorqueur(idDemande);

        return ResponseEntity.status(HttpStatus.OK).body(resVerif);
      }
      catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur");
      }

    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("l'id demande ne peut pas étre null");
  }

  @PutMapping("/annulerDemandeParClient/{idDemande}")
  public ResponseEntity<Object> annulerDemandeParClient (@PathVariable Long idDemande) {
    if(idDemande != null) {
      try {
        DemandeRemorquage demandeRemorquage = demandeRemorquageRepository.findById(idDemande).get();
        demandeRemorquage.setIsCanceledByClient(true);
        demandeRemorquageRepository.save(demandeRemorquage);
        return ResponseEntity.status(HttpStatus.OK).body("modifié avec succés") ;
      }
      catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur lors de l'opération de mise à jour") ;
      }
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur : idDemande ne peut pas étre null") ;
  }

  @GetMapping("/getDemande/{idDemande}")
  public ResponseEntity<Object> getDemande(@PathVariable Long idDemande) {
    if(idDemande != null ) {
      try{
        DemandeRemorquage demandeRemorquage = demandeRemorquageRepository.findById(idDemande).get();
        return ResponseEntity.status(HttpStatus.OK).body(demandeRemorquage);
      }
      catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }

    }
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }
  @GetMapping("/getRemorqeurFromDemande/{idDemande}")
  public ResponseEntity<Object>  getRemorqeurFromDemande(@PathVariable Long idDemande) {
    if(idDemande != null) {
      try{
        DemandeRemorquage demandeRemorquage = demandeRemorquageRepository.findById(idDemande).get();
        Remorqueur remorqueur = demandeRemorquage.getRemorqueur();
        return ResponseEntity.status(HttpStatus.OK).body(remorqueur);
      }
      catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
      }
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }
  @GetMapping("/getAll/{idRemorqeur}")
  //@PreAuthorize("hasRole('REMORQEUR')")
  public ResponseEntity< List<DemandeRemorquage> > getListeDemandes(@PathVariable Long idRemorqeur) {
    if(idRemorqeur != null) {
      List<DemandeRemorquage> listeDemandeRemorquage = demandeRemorquageRepository.findAll();
      //
      List<DemandeRemorquage> liste = new ArrayList<>();
      for (DemandeRemorquage d:listeDemandeRemorquage ) {
        if(d.getRemorqueur() == null || (d.getRemorqueur().getId() != idRemorqeur &&  d.getIsDeclined())) liste.add(d);
      }
      return ResponseEntity.status(HttpStatus.OK).body(liste);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);




  }

  @PutMapping("/accepterDemande/")
  public ResponseEntity<Object> accepterDemande(@RequestBody DemandeRemorquageAccepteDto demandeRemorquageAccepteDto) {
   if(demandeRemorquageAccepteDto.getIdDemande() != null && demandeRemorquageAccepteDto.getIdRemorqeur() != null ) {
     Optional<Remorqueur> remorqueur  = remorqueurService.getRemorqueur(demandeRemorquageAccepteDto.getIdRemorqeur() );
     Optional<DemandeRemorquage> demande  = demandeRemorquageRepository.findById(demandeRemorquageAccepteDto.getIdDemande());

     try {
       Instant now = Instant.now();
       Timestamp dateAcceptation = Timestamp.from(now);

       demande.get().setRemorqueur(remorqueur.get());
       demande.get().setIsDeclined(false);
       demande.get().setDurreeInMinutes(demandeRemorquageAccepteDto.getDureeInMin());
       demande.get().setDateAcceptation(dateAcceptation);

       demandeRemorquageRepository.save(demande.get());
       return ResponseEntity.status(HttpStatus.OK).body(demande);
     }
     catch (Exception e) {
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("erreur");
     }

   }


    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("idDemande et idRemorqeur ne peuvent pas étre null");
  }

 @PostMapping("/finirCourse/{idDemande}/{isFinished}")
 public  ResponseEntity<Object> finirCourse (@PathVariable Long idDemande , @PathVariable Boolean isFinished) {
    if(idDemande != null && isFinished != null) {
      try{
        Optional<DemandeRemorquage> demandeRemorquage = demandeRemorquageRepository.findById(idDemande);
        demandeRemorquage.get().setIsFinished(isFinished);
        demandeRemorquageRepository.save(demandeRemorquage.get());
        return ResponseEntity.status(HttpStatus.OK).body(demandeRemorquage.get());
      }
      catch (Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body("error");
 }

  @PostMapping("/updateCourse/{idDemande}/{isClientPickedUp}")
  public  ResponseEntity<Object> updateCourse (@PathVariable Long idDemande , @PathVariable Boolean isClientPickedUp) {
    if(idDemande != null && isClientPickedUp != null) {
      try{
        Optional<DemandeRemorquage> demandeRemorquage = demandeRemorquageRepository.findById(idDemande);
        demandeRemorquage.get().setIsClientPickedUp(isClientPickedUp);
        demandeRemorquageRepository.save(demandeRemorquage.get());
        return ResponseEntity.status(HttpStatus.OK).body(demandeRemorquage.get());
      }
      catch (Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
      }
    }
    return ResponseEntity.status(HttpStatus.OK).body("error");
  }

  @PostMapping("/declineDemande/{idConsommateur}/{idRemorqeur}/{idDemande}")
  //@PreAuthorize("hasRole('REMORQEUR')")
  public ResponseEntity< Object > declineDemande(@PathVariable Long idConsommateur ,@PathVariable Long idRemorqeur ,@PathVariable Long idDemande  ) {
    if(idConsommateur != null && idRemorqeur != null && idDemande != null) {
      Optional<Consommateur> consommateur = consommateurService.getConsommateur(idConsommateur);
      Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(idRemorqeur);

      if (consommateur.get() != null) {

        try {
          List<DemandeRemorquage> liste = consommateur.get().getListeDemandesRemorquage();

          for (DemandeRemorquage d : liste) {
            if (d.getId() == idDemande) {

              d.setIsDeclined(true);
              d.setRemorqueur(remorqueur.get());

            }
          }

          consommateur.get().setListeDemandesRemorquage(liste);
          consommateurService.saveOrUpdateConsommateur(consommateur.get());
          return ResponseEntity.status(HttpStatus.OK).body("succes");
        } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("echec");
        }
      }
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("verifier l'id de la demande");
  }
  }
