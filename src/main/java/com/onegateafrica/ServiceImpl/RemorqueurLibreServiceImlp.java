package com.onegateafrica.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.RemorqueurLibre;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Repositories.RemorqueurLibreRepository;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Service.RemorqueurLibreService;

@Service
@Transactional
public class RemorqueurLibreServiceImlp implements RemorqueurLibreService {
	@Autowired
	private RemorqueurLibreRepository remorqueurLibreRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public RemorqueurLibre saveRemorqueurLibre(RemorqueurLibre remorqueurLibre) {
		return remorqueurLibreRepository.save(remorqueurLibre);
	}



	@Override
	public RemorqueurLibre findByEmail(String email) {
		return remorqueurLibreRepository.findByEmail(email);
	}

	@Override
	public List<RemorqueurLibre> getRemorqueurLibres() {
		return remorqueurLibreRepository.findAll();
	}

	@Override
	public Optional<RemorqueurLibre> getRemorqueurLibre(Long id) {
		return remorqueurLibreRepository.findById(id);
	}

	@Override
	public void deleteRemorqueurLibre(Long id) {
		remorqueurLibreRepository.deleteById(id);
	}

	@Override
	public Boolean existsByEmail(String email) {
		if (remorqueurLibreRepository.existsByEmail(email)) {
			return true;
		}

		return false;
	}

	@Override
	public RemorqueurLibre getRemorqueurLibreByPhoneNumber(String PhoneNumber) {
		return remorqueurLibreRepository.findByPhoneNumber(PhoneNumber);
	}

}
