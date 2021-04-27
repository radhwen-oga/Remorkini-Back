package com.onegateafrica.Service;

import java.util.List;
import java.util.Optional;

import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.RemorqueurLibre;

public interface RemorqueurLibreService {
	public RemorqueurLibre saveRemorqueurLibre(RemorqueurLibre remorqueurLibre);

	public RemorqueurLibre findByEmail(String email);
	public List<RemorqueurLibre> getRemorqueurLibres();
	public Optional<RemorqueurLibre> getRemorqueurLibre(Long id);
	public void  deleteRemorqueurLibre(Long id);
	public Boolean existsByEmail(String email);
	public RemorqueurLibre getRemorqueurLibreByPhoneNumber(String PhoneNumber);

}
