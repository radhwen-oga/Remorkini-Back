package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.DemandeRemorquage;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Payloads.request.DemandeRemorquageDto;
import com.onegateafrica.Repositories.DemandeRemorquageRepository;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/demandeRemorquage")

public class DemandeRemorquageController {

  private final RemorqueurService remorqueurService ;
  private final DemandeRemorquageRepository demandeRemorquageRepository ;
  private final ConsommateurService consommateurService ;

  @Autowired
  public DemandeRemorquageController(RemorqueurService remorqueurService, DemandeRemorquageRepository demandeRemorquageRepository, ConsommateurService consommateurService) {
    this.remorqueurService = remorqueurService;
    this.demandeRemorquageRepository = demandeRemorquageRepository;
    this.consommateurService = consommateurService;
  }

  @PostMapping("/addDemande")
  //@PreAuthorize("hasRole('CONSOMMATEUR')")
  public ResponseEntity< Object > addDemandeRemorquage(@RequestBody DemandeRemorquageDto demandeRemorquageDto){

   if(demandeRemorquageDto != null) {
     try{
       Optional<Consommateur> consommateur = consommateurService.getConsommateur(demandeRemorquageDto.getIdConsommateur());

       DemandeRemorquage demandeRemorquage = new DemandeRemorquage();
       Consommateur entity = consommateur.get();

       demandeRemorquage.setConsommateur(entity);
       //demandeRemorquage.setDescription(demandeRemorquageDto.getDescription());
       demandeRemorquage.setMarqueVoiture(demandeRemorquageDto.getMarqueVoiture());
       demandeRemorquage.setNbrePersonnes(demandeRemorquageDto.getNbrePersonnes());
       demandeRemorquage.setTypePanne(demandeRemorquageDto.getTypePanne());

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

  @PostMapping("/accepterDemande/{idDemande}/{idRemorqeur}")
  public ResponseEntity<Object> accepterDemande(@PathVariable Long idDemande ,@PathVariable Long idRemorqeur ) {
   if(idDemande != null && idRemorqeur != null ) {
     Optional<Remorqueur> remorqueur  = remorqueurService.getRemorqueur(idRemorqeur);
     Optional<DemandeRemorquage> demande  = demandeRemorquageRepository.findById(idDemande);

     if(remorqueur.get() !=null  && demande.get() !=null   ) {
       demande.get().setRemorqueur(remorqueur.get());
       demande.get().setIsDeclined(false);
       demandeRemorquageRepository.save(demande.get());
       return ResponseEntity.status(HttpStatus.OK).body(demande);
     }
   }


    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur");
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
