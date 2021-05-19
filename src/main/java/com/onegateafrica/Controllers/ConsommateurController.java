package com.onegateafrica.Controllers;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//import com.onegateafrica.Entities.Remorqueur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onegateafrica.Controllers.utils.DataValidationUtils;
import com.onegateafrica.Controllers.utils.ImageIO;
import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Service.ConsommateurService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/signUp")
public class ConsommateurController {
	private final ConsommateurService consommateurService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private static String imageDirectory = System.getProperty("user.dir") + "/images/";
	private final RoleRepository roleRepository;

	@Autowired
	public ConsommateurController(RoleRepository roleRepository,ConsommateurService consommateurService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.consommateurService = consommateurService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.roleRepository=roleRepository;

	}
	private void makeDirectoryIfNotExist(String imageDirectory) {
		File directory = new File(imageDirectory);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}


	@PutMapping("/updateConsommateur")
	public Consommateur updateClient(@RequestBody Consommateur consommateur) {
		Consommateur consommateur1 = consommateurService.getConsommateurByPhoneNumber(consommateur.getPhoneNumber());
		consommateurService.saveOrUpdateConsommateur(consommateur1);
		return consommateur;
	}

	@PostMapping("/signupConsommateur")
	public ResponseEntity < String > registerClient(
			@RequestParam String password,
			@RequestParam String email,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam String phoneNumber,
			@RequestParam( name="photoProfileFile",required = false) MultipartFile photoProfileFile) {
		if (DataValidationUtils.isValid(phoneNumber)) {
			Consommateur user = consommateurService.getConsommateurByPhoneNumber(phoneNumber);
			Consommateur insertableUser = new Consommateur();
			if (user != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone number already exists.");
			} else {
				if (DataValidationUtils.isValid(firstName) &&
						DataValidationUtils.isValid(lastName) &&
						DataValidationUtils.isValid(email)) {
					insertableUser.setEmail(email);
					insertableUser.setFirstName(firstName);
					insertableUser.setLastName(lastName);
					insertableUser.setUserName(firstName+lastName);
					insertableUser.setPassword(bCryptPasswordEncoder.encode(password));
					insertableUser.setPhoneNumber(phoneNumber);
					Set<Role> roles = new HashSet<>();
					Optional<Role> role = roleRepository.findByRoleName(ERole.ROLE_CONSOMMATEUR);
					if(role.get()!= null)
						roles.add(role.get());
					else
						throw new RuntimeException("role not found");
					insertableUser.setRoles(roles);
					if (photoProfileFile == null) {
						insertableUser.setUserPicture("DEFAULT");
						consommateurService.saveOrUpdateConsommateur(insertableUser);
						return ResponseEntity.status(HttpStatus.ACCEPTED).body("OK");
					}
					else {
						String photoProfilFileName = phoneNumber + "_" + photoProfileFile.getOriginalFilename();
						Boolean isPhotoProfilUploaded = ImageIO.uploadImage(photoProfileFile, photoProfilFileName);
						if (isPhotoProfilUploaded == true && !photoProfileFile.isEmpty()) {
							insertableUser.setUserPicture(photoProfilFileName);
							consommateurService.saveOrUpdateConsommateur(insertableUser);
							return ResponseEntity.status(HttpStatus.ACCEPTED).body("OK");
						} else if (isPhotoProfilUploaded == false) {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error loading photo");
						} else {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD REQUEST");
						}
					}
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NULL");
				}
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check phone number");
		}
	}


	@GetMapping("/findAllConsommateur")
	public List < Consommateur > getClients() {
		return consommateurService.getConsommateurs();
	}

	@GetMapping("/findConsommateur/{id}")
	public Optional < Consommateur > getClient(@PathVariable Long id) {
		if (DataValidationUtils.isValidId(id)) {
			return consommateurService.getConsommateur(id);
		} else {
			return null;
		}
	}

	@DeleteMapping("/deleteConsommateur/{id}")
	public ResponseEntity < String > deleteClient(@PathVariable Long id) {
		try {
			if (DataValidationUtils.isValidId(id)) {
				consommateurService.deleteConsommateur(id);
				return ResponseEntity.status(HttpStatus.OK).body("deleted");
			} else {
				return null;
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete client");
		}
	}

	@GetMapping("/getConsommateurByPhoneNumber/{PhoneNumber}")
	public Consommateur getClientByPhoneNumber(@PathVariable String PhoneNumber) {
		if (DataValidationUtils.isValid(PhoneNumber)) {
			return consommateurService.getConsommateurByPhoneNumber(PhoneNumber);
		} else {
			return null;
		}
	}



	//ajouté par radhwen ticket 1612
	@GetMapping("/consommateur/{id}")
	//@PreAuthorize("hasRole('CONSOMMATEUR')")
	public ResponseEntity<Consommateur> getConsommateurById (@PathVariable Long id) {
		Optional<Consommateur> consommateur = consommateurService.getConsommateur(id);
		if(consommateur.get() != null) {
			return ResponseEntity.status(HttpStatus.FOUND).body(consommateur.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(consommateur.get());
	}

	////////////////

	//ajouté par radhwen ticket 1612
	@GetMapping("/getConsommateurWithRemorqeur/{idConsommateur}")
	//@PreAuthorize("hasRole('REMORQEUR')")
	public ResponseEntity<Object> getConsommateurWithRemorqeur( @PathVariable  Long idConsommateur) {
		if(idConsommateur != null && idConsommateur>0 ) {
			Optional<Consommateur> consommateur = consommateurService.getConsommateur(idConsommateur);

			if(consommateur.get() != null ){
				consommateur.get().getRemorqueur();

				return ResponseEntity.status(HttpStatus.FOUND).body(consommateur.get().getRemorqueur());
			}
			else {return ResponseEntity.status(HttpStatus.NOT_FOUND).body(consommateur.get());}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("please provide right path variables");
	}
}