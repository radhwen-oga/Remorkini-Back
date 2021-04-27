package com.onegateafrica.Service;

import java.util.List;
import java.util.Optional;

import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Utilisateur;

public interface AccountService {
	public Utilisateur saveUser(Utilisateur user);

	public Utilisateur findByEmail(String email);
	public List<Utilisateur> getUsers();
	public Optional<Utilisateur> getUser(Long id);
	public void  deleteUser(Long id);
	public Boolean existsByEmail(String email);
	public Utilisateur getUserByPhoneNumber(String PhoneNumber);

}
