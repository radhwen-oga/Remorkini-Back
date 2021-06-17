package com.onegateafrica.ServiceImpl;

import com.onegateafrica.Entities.Bannissement;
import com.onegateafrica.Entities.Reclamation;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Payloads.response.BannResponse;
import com.onegateafrica.Repositories.BannissementRepository;
import com.onegateafrica.Service.BannissementService;
import com.onegateafrica.Service.ReclamationService;
import com.onegateafrica.Service.RemorqueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BannissementServiceImpl implements BannissementService {



    private final BannissementRepository bannissementRepository ;
    private final RemorqueurService remorqueurService ;


    @Autowired
    public BannissementServiceImpl(BannissementRepository bannissementRepository, RemorqueurService remorqueurService ) {
        this.bannissementRepository = bannissementRepository;

        this.remorqueurService = remorqueurService;


    }

    @Override
    public Bannissement saveOrUpdateBannissement(Bannissement bannissement) {
        return bannissementRepository.save(bannissement);
    }

    @Override
    public List<Bannissement> getBannisements() {
        return bannissementRepository.findAll();
    }

    @Override
    public Optional<Bannissement> getBannissement(Long id) {
        return bannissementRepository.findById(id);
    }

    @Override
    public void deleteBannissement(Long id) {
        bannissementRepository.deleteById(id);
    }

    @Override
    public Optional<List<Bannissement>> getBannissementOfRemorqeur(long idRemorqueur) {
        return bannissementRepository.getBannissementOfRemorqeur(idRemorqueur);
    }

    @Override
    public BannResponse verifierBann(Long idRemorqeur) {
        Remorqueur remorqueur = remorqueurService.getRemorqueur(idRemorqeur).get() ;
        BannResponse bannResponse  =new BannResponse() ;
        bannResponse.setBanned(false);

        if(remorqueur.getIsBanned() == true) {
            System.out.println("le remorqeur est banni ");


            List<Bannissement> bannissementListOfRemorqueur = remorqueur.getListeBannissements() ;
            Bannissement dernierBannNonTermine =null;

            //get le bann en question => (dernier bann non terminé )
            for (Bannissement ban : bannissementListOfRemorqueur) {
              if(ban.getIsCompleted() == false) {
                  dernierBannNonTermine = ban ;


              }

            }

            //verifier si la date fin du bann est passé
            Instant now = Instant.now();
            Timestamp today = Timestamp.from(Instant.now());


            System.out.println("resultat de compare "+today.compareTo(dernierBannNonTermine.getDateFinBann()) );
            //si la date du bann est passé => mettre isbanned =false
            if(today.compareTo(dernierBannNonTermine.getDateFinBann()) > 0) {
                //Bannissement bannissement = bannissementService.getBannissement(ba)
                dernierBannNonTermine.setIsCompleted(true);
                remorqueur.setIsBanned(false);
                remorqueurService.saveOrUpdateRemorqueur(remorqueur);
                bannResponse.setBanned(false);
            }
            else {
                bannResponse.setDateDebutBann(dernierBannNonTermine.getDateDebutBann().toString());
                bannResponse.setDateFinBann(dernierBannNonTermine.getDateFinBann().toString());
                bannResponse.setBanned(true);
            }

        }
        return bannResponse;
    }
}
