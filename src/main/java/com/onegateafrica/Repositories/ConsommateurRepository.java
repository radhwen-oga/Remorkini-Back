package com.onegateafrica.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onegateafrica.Entities.Consommateur;

public interface ConsommateurRepository extends JpaRepository<Consommateur, Long> {

	public Consommateur findByPhoneNumber(String PhoneNumber);

	public Consommateur findByEmail(String email);

	Boolean existsByEmail(String email);

}
