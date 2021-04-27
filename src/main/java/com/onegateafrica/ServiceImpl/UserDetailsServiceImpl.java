package com.onegateafrica.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



import com.onegateafrica.Entities.Utilisateur;
import com.onegateafrica.Repositories.UtilisateurRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired 
	UtilisateurRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Utilisateur user = userRepository.findByEmail(email);
				if(user==null) throw new UsernameNotFoundException(email);

		return UserDetailsImpl.build(user);
	}

}