package com.onegateafrica.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onegateafrica.Entities.Utilisateur;
import com.onegateafrica.Service.AccountService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UtilisateurController {

	private final AccountService accountService;

	@Autowired
	public UtilisateurController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping("/findAllUsers")
	public List<Utilisateur> getUsers() {
		return accountService.getUsers();
	}

	@GetMapping("/getUserByPhoneNumber/{PhoneNumber}")
	public Utilisateur getUserByPhoneNumber(@PathVariable String PhoneNumber) {
		return accountService.getUserByPhoneNumber(PhoneNumber);
	}


}
