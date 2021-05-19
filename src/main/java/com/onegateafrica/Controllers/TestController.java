package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.Consommateur;

import com.onegateafrica.Entities.Demande;
import com.onegateafrica.Entities.DemandeRemorquage;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Payloads.request.DemandeRemorquageDto;
import com.onegateafrica.Repositories.DemandeRemorquageRepository;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/test")
public class TestController {




    private final RemorqueurService remorqueurService ;
    private final DemandeRemorquageRepository demandeRemorquageRepository ;
    private final ConsommateurService consommateurService ;
    @Autowired
    public TestController(RemorqueurService remorqueurService, DemandeRemorquageRepository demandeRemorquageRepository, ConsommateurService consommateurService) {
        this.remorqueurService = remorqueurService;
        this.demandeRemorquageRepository = demandeRemorquageRepository;
        this.consommateurService = consommateurService;
    }

    @GetMapping("/consommateur")
    @PreAuthorize("hasRole('CONSOMMATEUR')")
    public String welcomeConsommateur(){
        return "welcome consommateur";
    }

    @GetMapping("/remorqeur")
    @PreAuthorize("hasRole('REMORQEUR')")
    public String welcomeRemorqeur(){
        return "welcome remorqeur";
    }

    @GetMapping("/remorqeur/{id}")
    @PreAuthorize("hasRole('REMORQEUR')")
    public ResponseEntity< Remorqueur > getRemorqeurByCin (@PathVariable Long id) {
        Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(id);
        if(remorqueur.get() != null) {
            return ResponseEntity.status(HttpStatus.FOUND).body(remorqueur.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(remorqueur.get());
    }


    @PostMapping("/consommateur/addDemande")
    @PreAuthorize("hasRole('CONSOMMATEUR')")
    public ResponseEntity< Object > addDemandeRemorquage( @RequestBody DemandeRemorquageDto demandeRemorquage){
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

    @GetMapping("/demandeRemorquage/getAll")
    @PreAuthorize("hasRole('REMORQEUR')")
    public ResponseEntity< List<DemandeRemorquage> > getListeDemandes() {
        List<DemandeRemorquage> listeDemandeRemorquage = demandeRemorquageRepository.findAll();

            List<DemandeRemorquage> liste = new ArrayList<>();
            for (DemandeRemorquage d:listeDemandeRemorquage ) {
                if(d.getRemorqueur() == null) liste.add(d);
            }
            return ResponseEntity.status(HttpStatus.FOUND).body(liste);



    }
}
