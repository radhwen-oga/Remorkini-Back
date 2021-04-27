package com.onegateafrica.ServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Entities.Utilisateur;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Repositories.UtilisateurRepository;
import com.onegateafrica.Service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private  UtilisateurRepository utilisateurRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Utilisateur saveUser(Utilisateur user) {

		return utilisateurRepository.save(user);
	}



	@Override
	public Utilisateur findByEmail(String email) {
		return utilisateurRepository.findByEmail(email);
	}

	@Override
	public List<Utilisateur> getUsers() {
		return utilisateurRepository.findAll();

	}

	@Override
	public Optional<Utilisateur> getUser(Long id) {
		return utilisateurRepository.findById(id);

	}

	@Override
	public void deleteUser(Long id) {
		utilisateurRepository.deleteById(id);

	}

	@Override
	public Boolean existsByEmail(String email) {
		if (utilisateurRepository.existsByEmail(email)) {
			return true;
		}
		return false;
	}

	@Override
	public Utilisateur getUserByPhoneNumber(String PhoneNumber) {
		return utilisateurRepository.findByPhoneNumber(PhoneNumber);
	}

}
