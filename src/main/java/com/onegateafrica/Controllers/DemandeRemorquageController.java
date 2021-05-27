package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.DemandeRemorquage;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Payloads.request.DemandeRemorquageDto;
import com.onegateafrica.Repositories.DemandeRemorquageRepository;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/*

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/demandeRemorquage")
*/
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
  @PreAuthorize("hasRole('CONSOMMATEUR')")
  public ResponseEntity< Object > addDemandeRemorquage(@RequestBody DemandeRemorquageDto demandeRemorquage){
    Optional<Consommateur> consommateur = consommateurService.getConsommateur(demandeRemorquage.getIdConsommateur());

    DemandeRemorquage demande = new DemandeRemorquage();
    Consommateur entity = consommateur.get();
    demande.setConsommateur(entity);

    List<DemandeRemorquage> listeDemandeRemorquage = new ArrayList<>();
    listeDemandeRemorquage.add(demande);

    entity.setListeDemandesRemorquage(listeDemandeRemorquage);
    try{
      consommateurService.saveOrUpdateConsommateur(entity);
      return ResponseEntity.status(HttpStatus.OK).body("ajout fait avec succés");
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failure");
    }


  }

  @GetMapping("/getAll/{idRemorqeur}")
  //@PreAuthorize("hasRole('REMORQEUR')")
  public ResponseEntity< List<DemandeRemorquage> > getListeDemandes(@PathVariable Long idRemorqeur) {
    List<DemandeRemorquage> listeDemandeRemorquage = demandeRemorquageRepository.findAll();
    //
    List<DemandeRemorquage> liste = new ArrayList<>();
    for (DemandeRemorquage d:listeDemandeRemorquage ) {
      if(d.getRemorqueur() == null || (d.getRemorqueur().getId() != idRemorqeur &&  d.isDeclined())) liste.add(d);
    }
    return ResponseEntity.status(HttpStatus.OK).body(liste);



  }

  @GetMapping("/accepterDemande/{idDemande}/{idRemorqeur}")
  public ResponseEntity<Object> accepterDemande(@PathVariable Long idDemande ,@PathVariable Long idRemorqeur ) {
    Optional<Remorqueur> remorqueur  = remorqueurService.getRemorqueur(idRemorqeur);
    Optional<DemandeRemorquage> demande  = demandeRemorquageRepository.findById(idDemande);

    if(remorqueur.get() !=null && remorqueur.get().getId()> 0 && demande.get() !=null && demande.get().getId()>0  ) {
      demande.get().setRemorqueur(remorqueur.get());
      demande.get().setDeclined(false);
      demandeRemorquageRepository.save(demande.get());
      return ResponseEntity.status(HttpStatus.OK).body(demande);
    }
    return ResponseEntity.status(HttpStatus.OK).body("erreur");
  }
  @GetMapping("/declineDemande/{idConsommateur}/{idRemorqeur}/{idDemande}")
  //@PreAuthorize("hasRole('REMORQEUR')")
  ////
  public ResponseEntity< Object > declineDemande(@PathVariable Long idConsommateur ,@PathVariable Long idRemorqeur ,@PathVariable Long idDemande  ) {
    Optional<Consommateur> consommateur = consommateurService.getConsommateur(idConsommateur);
    Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(idRemorqeur);

    if (consommateur.get() != null) {

      try {
        List<DemandeRemorquage> liste = consommateur.get().getListeDemandesRemorquage();

        for (DemandeRemorquage d : liste) {
          if (d.getId() == idDemande) {

            d.setDeclined(true);
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
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("verifier l'id de la demande");
  }
  }
