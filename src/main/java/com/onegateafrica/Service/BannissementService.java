package com.onegateafrica.Service;

import com.onegateafrica.Entities.Bannissement;
import com.onegateafrica.Entities.Reclamation;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BannissementService {
    Bannissement saveOrUpdateBannissement(Bannissement bannissement);

    List<Bannissement> getBannisements();

    Optional<Bannissement> getBannissement(Long id);

    void deleteBannissement(Long id);

    Optional<List<Bannissement>> getBannissementOfRemorqeur( long idRemorqueur ) ;


}

