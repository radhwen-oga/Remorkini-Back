package com.onegateafrica.Controllers;

import com.onegateafrica.Controllers.utils.DataValidationUtils;
import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Payloads.request.LoginForm;
import com.onegateafrica.Payloads.request.SignUpForm;
import com.onegateafrica.Payloads.response.JwtResponse;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Security.jwt.JwtUtils;
import com.onegateafrica.ServiceImpl.UserDetailsImpl;
import com.onegateafrica.Service.ConsommateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final ConsommateurService consommateurService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;


    @Autowired
    public AuthenticationController(ConsommateurService consommateurService, AuthenticationManager authenticationManager,
                                    BCryptPasswordEncoder bCryptPasswordEncoder, JwtUtils jwtUtils,
                                    RoleRepository roleRepository) {
        this.consommateurService = consommateurService;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
    }


    @PostMapping("/signin")
    public ResponseEntity<?> AuthenticatedUserRealm(@RequestBody LoginForm loginRequest) {

      Optional<Consommateur> consommateur = consommateurService.getConsommateurByEmail(loginRequest.getEmail());
      if (consommateur.isPresent()) {
        if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), consommateur.get().getPassword())) {

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

          List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());
          return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              roles,
              userDetails.getPhoneNumber(),
              userDetails.getFirstName(),
              userDetails.getLastName()));
        } else {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check email or password");
        }
      } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NOT FOUND");
      }
    }

    @PostMapping("/signupConsommateur")
    public ResponseEntity<String> registerClient(@RequestBody SignUpForm body) {
        if (DataValidationUtils.isValid(body.getPhoneNumber())) {
            if (consommateurService.getConsommateurByPhoneNumber(body.getPhoneNumber()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone number already exists.");
            }
            if (consommateurService.getConsommateurByEmail(body.getEmail()).isPresent()) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists.");
            }
            Consommateur consommateur = new Consommateur();
            if (DataValidationUtils.isValid(body.getFirstName()) &&
                    DataValidationUtils.isValid(body.getLastName()) &&
                    DataValidationUtils.isValid(body.getEmail())) {
                consommateur.setEmail(body.getEmail());
                consommateur.setFirstName(body.getFirstName());
                consommateur.setLastName(body.getLastName());
                consommateur.setUserName(body.getFirstName() + body.getLastName());
                consommateur.setPassword(bCryptPasswordEncoder.encode(body.getPassword()));
                consommateur.setPhoneNumber(body.getPhoneNumber());
                Set<Role> roles = new HashSet<>();
                Optional<Role> role = roleRepository.findByRoleName(ERole.ROLE_CONSOMMATEUR);
                if (role.get() != null)
                    roles.add(role.get());
                else
                    throw new RuntimeException("role not found");
                consommateur.setRoles(roles);
                consommateur.setUserPicture("DEFAULT");
                consommateurService.saveOrUpdateConsommateur(consommateur);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("OK");

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID FIELDS");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check phone number");
        }
    }
}
