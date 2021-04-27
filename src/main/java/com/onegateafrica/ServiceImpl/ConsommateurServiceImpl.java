package com.onegateafrica.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Repositories.ConsommateurRepository;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Service.ConsommateurService;

@Service
@Transactional
public class ConsommateurServiceImpl implements ConsommateurService {

	@Autowired
	private ConsommateurRepository consommateurRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Consommateur saveConsommateur(Consommateur consommateur) {
		return consommateurRepository.save(consommateur);
	}



	@Override
	public Consommateur findByEmail(String email) {
		return consommateurRepository.findByEmail(email);
	}

	@Override
	public List<Consommateur> getConsommateurs() {
		return consommateurRepository.findAll();
	}

	@Override
	public Optional<Consommateur> getConsommateur(Long id) {
		return consommateurRepository.findById(id);
	}

	@Override
	public void deleteConsommateur(Long id) {
		consommateurRepository.deleteById(id);
	}

	@Override
	public Boolean existsByEmail(String email) {
		if (consommateurRepository.existsByEmail(email)) {
			return true;
		}

		return false;

	}

	@Override
	public Consommateur getConsommateurByPhoneNumber(String PhoneNumber) {
		return consommateurRepository.findByPhoneNumber(PhoneNumber);
	}

}
