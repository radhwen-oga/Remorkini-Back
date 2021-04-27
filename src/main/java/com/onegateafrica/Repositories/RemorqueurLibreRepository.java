package com.onegateafrica.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.onegateafrica.Entities.RemorqueurLibre;

@Repository
public interface RemorqueurLibreRepository extends JpaRepository<RemorqueurLibre, Long> {
	public RemorqueurLibre findByEmail(String email);	
	public RemorqueurLibre findByPhoneNumber(String PhoneNumber);
	Boolean existsByEmail(String email);
	
}
