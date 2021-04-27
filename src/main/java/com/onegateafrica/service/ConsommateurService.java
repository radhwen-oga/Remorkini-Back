package com.onegateafrica.Service;

import java.util.List;
import java.util.Optional;


import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;

public interface ConsommateurService {
	
	public Consommateur saveConsommateur(Consommateur user);


	public Consommateur findByEmail(String email);

	public List<Consommateur> getConsommateurs();

	public Optional<Consommateur> getConsommateur(Long id);

	public void deleteConsommateur(Long id);

	public Boolean existsByEmail(String email);
	
	public Consommateur getConsommateurByPhoneNumber(String PhoneNumber);

}
