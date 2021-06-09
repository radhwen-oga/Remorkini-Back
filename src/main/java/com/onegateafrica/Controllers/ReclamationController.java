package com.onegateafrica.Controllers;

import com.onegateafrica.Controllers.utils.IntervalWeekUtils;
import com.onegateafrica.Entities.Reclamation;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Payloads.request.ReclamationDto;
import com.onegateafrica.Payloads.request.SemaineIntervalleDto;
import com.onegateafrica.Service.ReclamationService;
import com.onegateafrica.Service.RemorqueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reclamation")
public class ReclamationController {

    private final ReclamationService reclamationService ;
    private final RemorqueurService remorqueurService ;

    @Autowired
    public ReclamationController(ReclamationService reclamationService, RemorqueurService remorqueurService) {
        this.reclamationService = reclamationService;
        this.remorqueurService = remorqueurService;
    }

    @PostMapping("/ajouterReclamation")
    public ResponseEntity<Object> addReclamation(@RequestBody ReclamationDto reclamationDto){
        if(reclamationDto !=null && reclamationDto.getIdRemorqeur() !=null && reclamationDto.getDescription() !=null){
            Remorqueur remorqueur ;

            try {
                remorqueur = remorqueurService.getRemorqueur(reclamationDto.getIdRemorqeur()).get();
                Reclamation reclamation = new Reclamation() ;
                if(!reclamationDto.getDescription().isEmpty()) reclamation.setDescription(reclamationDto.getDescription());
                reclamation.setRemorqueur(remorqueur);

                Instant today = Instant.now();
                reclamation.setDateAjout(Date.from(today));

                remorqueur.getListeReclamations().add(reclamation);

                //remorqueurService.saveOrUpdateRemorqueur(remorqueur);

                //2) ------calculate the week of today's date
                IntervalWeekUtils currentIntervalWeek = reclamationService.calculateWeekFromToday(today);
                //IntervalWeekUtils intervalWeekUtils  = reclamationService.calculateWeekFromToday(today.plus(Duration.ofDays(6)));
                System.out.println("this is from controller "+currentIntervalWeek.getLeftDateIntervall()+" " +currentIntervalWeek.getRightDateIntervall());

                //3)---------- get the list of reclamations of a given remorqeur in this week
                List<Reclamation> allReclamationsList = reclamationService.getReclamationsOfRemorqeur(reclamationDto.getIdRemorqeur()).get();
                System.out.println(allReclamationsList.size());
               List<Reclamation> searchedListOfReclmations=  reclamationService.getReclamationsOfWeek(allReclamationsList,currentIntervalWeek.getLeftDateIntervall(),currentIntervalWeek.getRightDateIntervall());


                return ResponseEntity.status(HttpStatus.OK).body(searchedListOfReclmations);

            }
            catch (Exception e){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("erreur ");
            }


        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("vérifiez les données passés");
    }


}
