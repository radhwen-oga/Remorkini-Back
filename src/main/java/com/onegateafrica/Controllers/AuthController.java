package com.onegateafrica.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Utilisateur;
import com.onegateafrica.Payloads.request.LoginForm;
import com.onegateafrica.Payloads.request.SignUpForm;
import com.onegateafrica.Payloads.response.JwtResponse;
import com.onegateafrica.Security.jwt.JwtUtils;
import com.onegateafrica.Service.AccountService;
import com.onegateafrica.ServiceImpl.UserDetailsImpl;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	private final AccountService accountService;

	@Autowired
	public AuthController(AccountService accountService) {
		this.accountService = accountService;
	}

	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> AuthenticatedUserRealm(@RequestBody LoginForm loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
	}
}
