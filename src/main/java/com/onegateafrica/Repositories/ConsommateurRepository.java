package com.onegateafrica.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.onegateafrica.Entities.Consommateur;

public interface ConsommateurRepository extends JpaRepository<Consommateur, Long> {



	   Consommateur findByPhoneNumber(String PhoneNumber);


	   Boolean existsByEmail(String email);


	   Optional<Consommateur> findByEmail(String email);




}

