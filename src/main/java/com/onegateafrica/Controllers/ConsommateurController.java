package com.onegateafrica.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Payloads.request.SignUpForm;
import com.onegateafrica.Service.ConsommateurService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ConsommateurController {
	private final ConsommateurService consommateurService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public ConsommateurController(ConsommateurService consommateurService) {
		this.consommateurService = consommateurService;
	}
	@PostMapping("/signupConsommateur")
	public Consommateur registerClient(@RequestBody SignUpForm signUpRequest) {

		if (consommateurService.existsByEmail(signUpRequest.getEmail())) {
			throw new RuntimeException("this User already exists");
		}
		else {
			Consommateur user = new Consommateur();
			user.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
			user.setEmail(signUpRequest.getEmail());
			user.setPhoneNumber(signUpRequest.getPhoneNumber());
			user.setFirstName(signUpRequest.getFirstName());
			user.setLastName(signUpRequest.getLastName());
			consommateurService.saveConsommateur(user);
			//consommateurService.addRoleToConsommateur(signUpRequest.getEmail(), ERole.ClIENT);
			return user;
		}
	}

	@GetMapping("/findAllConsommateur")
	public List<Consommateur> getClients() {
		return consommateurService.getConsommateurs();
	}

	@GetMapping("/findConsommateur/{id}")
	public Optional<Consommateur> getClient(@PathVariable Long id) {
		return consommateurService.getConsommateur(id);
	}

	@DeleteMapping("/deleteConsommateur/{id}")
	public ResponseEntity<String> deleteClient(@PathVariable Long id) {
		try {
			consommateurService.deleteConsommateur(id);
			return ResponseEntity.status(HttpStatus.OK).body("deleted");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete client");
		}

	}

	@GetMapping("/getConsommateurByPhoneNumber/{PhoneNumber}")
	public Consommateur getClientByPhoneNumber(@PathVariable String PhoneNumber) {
		return consommateurService.getConsommateurByPhoneNumber(PhoneNumber);
	}

}
