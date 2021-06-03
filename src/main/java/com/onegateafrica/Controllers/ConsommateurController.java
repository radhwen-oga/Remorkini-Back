package com.onegateafrica.Controllers;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import javax.servlet.ServletContext;

import com.onegateafrica.Payloads.request.PushTokenDto;
import com.onegateafrica.Payloads.request.SignUpForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onegateafrica.Controllers.utils.DataValidationUtils;
import com.onegateafrica.Controllers.utils.ImageIO;
import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Service.ConsommateurService;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
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

	@PutMapping("/updateConsommateurPushToken/{idConsommateur}")
	public ResponseEntity<Object> updateConsommateurPushToken(@RequestBody PushTokenDto pushTokenDto , @PathVariable Long idConsommateur) {
		if(pushTokenDto !=null && pushTokenDto.getToken()!=null) {
			try {
				Consommateur consommateur = consommateurService.getConsommateur(idConsommateur).get();
				consommateur.setExpoPushToken(pushTokenDto.getToken());
				consommateurService.saveOrUpdateConsommateur(consommateur);
				return ResponseEntity.status(HttpStatus.CREATED) .body(consommateur);
			}
			catch(Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur");
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("le token ne peut pas étre null");
	}

	@PutMapping("/updateProfilePicture")
	public Consommateur updateClient(@RequestParam MultipartFile image, @RequestParam String phoneNumber) {

		Consommateur consommateur1 = consommateurService.getConsommateurByPhoneNumber(phoneNumber);
		String photoProfilFileName = phoneNumber + "_" + image.getOriginalFilename();
		Boolean isPhotoProfilUploaded = ImageIO.uploadImage(image, photoProfilFileName);
		if (isPhotoProfilUploaded == true && !image.isEmpty()) {
			consommateur1.setUserPicture(photoProfilFileName);
			consommateurService.saveOrUpdateConsommateur(consommateur1);
			return consommateur1;
		} else if (isPhotoProfilUploaded == false) {
			return null;
	}
		return consommateur1;
	}

	//@PostMapping("/signupConsommateur")

	/*@PostMapping("/signupConsommateur")
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
	}*/


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

	@GetMapping(value = "/cinPicture", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody
	byte[] getUserCINPicture(
			@RequestParam("phoneNumber") String phoneNumber
	) {

		/**
		 * http://localhost:8080/api/cinPicture?cinNumber=[cinNumber]
		 */
		if (DataValidationUtils.isValid(phoneNumber)) {
			Consommateur consommateur = consommateurService.getConsommateurByPhoneNumber(phoneNumber);
			String imageName = consommateur.getUserPicture();
			if (consommateur == null || imageName==null || imageName.isBlank()) {
				return ImageIO.getProfilImagePlaceholder();
			} else {
				try {
					byte[] image = ImageIO.getImage(imageName);
					return image;
				} catch (Exception ex) {
					ex.printStackTrace();
					return ImageIO.getProfilImagePlaceholder();
				}
			}
		} else {
			return ImageIO.getProfilImagePlaceholder();
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
}
