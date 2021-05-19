package com.onegateafrica.Service;

import com.onegateafrica.Entities.Remorqueur;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface RemorqueurService {


    Remorqueur saveOrUpdateRemorqueur(Remorqueur remorqueur);


    List<Remorqueur> getRemorqueurs();
    Optional<Remorqueur> getRemorqueur(Long id);
    void  deleteRemorqueur(Long id);

    Optional<Remorqueur> findRemorqeurByCIN(String cin);

    void updateDisponibility( long id,  boolean disponibility);

    List<Remorqueur> findAll();



}
