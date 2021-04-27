package com.onegateafrica.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.Entities.Utilisateur;
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

	 Utilisateur findByEmail(String email);
	Boolean existsByEmail(String email);
	public Utilisateur findByPhoneNumber(String PhoneNumber);

}
