package com.onegateafrica.ServiceImpl;

import com.onegateafrica.Entities.DemandeRemorquage;
import com.onegateafrica.Repositories.DemandeRemorquageRepository;
import com.onegateafrica.Service.DemandeRemorquageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class DemandeRemorquageServiceImpl implements DemandeRemorquageService {

    private final DemandeRemorquageRepository demandeRemorquageRepository ;

    @Autowired
    public DemandeRemorquageServiceImpl(DemandeRemorquageRepository demandeRemorquageRepository) {
        this.demandeRemorquageRepository = demandeRemorquageRepository;
    }

    @Override
    public boolean permettreChangementRemorqueur(Long idDemande) {
        DemandeRemorquage demandeRemorquage = demandeRemorquageRepository.findById(idDemande).get();

        Date now = Date.from(Instant.now());
        Instant dateAcceptationInInstant = demandeRemorquage.getDateAcceptation().toInstant();
        Date dateFin = Date.from(dateAcceptationInInstant.plus(Duration.ofMinutes(demandeRemorquage.getDurreeInMinutes())));

        System.out.println("this now "+now);
        System.out.println("tis is date fin "+dateFin);

        System.out.println("this is result of comparaison "+now.compareTo(dateFin));

        //verifier si on a dépassé la durée on retourne true sinon false
        if(now.compareTo((dateFin)) > 0) {
            return true ;
        }
        return false ;




    }
}
