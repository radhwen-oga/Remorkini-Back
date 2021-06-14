package com.onegateafrica.ServiceImpl;

import com.onegateafrica.Entities.Bannissement;
import com.onegateafrica.Repositories.BannissementRepository;
import com.onegateafrica.Service.BannissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BannissementServiceImpl implements BannissementService {



    private final BannissementRepository bannissementRepository ;

    @Autowired
    public BannissementServiceImpl(BannissementRepository bannissementRepository) {
        this.bannissementRepository = bannissementRepository;
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
}
