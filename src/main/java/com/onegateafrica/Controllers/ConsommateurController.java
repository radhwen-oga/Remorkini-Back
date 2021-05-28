package com.onegateafrica.Controllers;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import javax.servlet.ServletContext;

import com.onegateafrica.Payloads.request.SignUpForm;

import com.onegateafrica.Payloads.request.UpdateForm;
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
	public ResponseEntity<String> registerClient(@RequestBody UpdateForm body) {
		if (DataValidationUtils.isValid(body.getPhoneNumber())) {
			Consommateur consommateur = consommateurService.getConsommateur(body.getId()).get();
			if (DataValidationUtils.isValid(body.getFirstName()) &&
					DataValidationUtils.isValid(body.getLastName()) &&
					DataValidationUtils.isValid(body.getEmail())) {
				if(body.getEmail() != null && !body.getEmail().equals("")){
					consommateur.setEmail(body.getEmail());
				}
				if(body.getFirstName() != null && !body.getFirstName().equals("")){
					consommateur.setFirstName(body.getFirstName());
				}
				if(body.getLastName() != null && !body.getLastName().equals("")) {
					consommateur.setLastName(body.getLastName());
				}
				if(body.getFirstName() != null && !body.getFirstName().equals("")) {
					if(body.getLastName() != null && !body.getLastName().equals("")) {
						consommateur.setUserName(body.getFirstName() + body.getLastName());
					}
					else {
						consommateur.setUserName(body.getFirstName() + consommateur.getLastName());
					}
				}
				else{
					if(body.getLastName() != null && !body.getLastName().equals("")) {
						consommateur.setUserName(consommateur.getFirstName() + body.getLastName());
					}
				}
				if(body.getPassword() != null && !body.getPassword().equals("")) {
					consommateur.setPassword(bCryptPasswordEncoder.encode(body.getPassword()));
				}
				if(body.getPhoneNumber() != null && !body.getPhoneNumber().equals("")) {
					consommateur.setPhoneNumber(body.getPhoneNumber());
				}

				consommateurService.saveOrUpdateConsommateur(consommateur);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("OK");

			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID FIELDS");
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check phone number");
		}
	}


	@PutMapping("/updateProfilePicture")
	public ResponseEntity<String> updateClient(@RequestParam MultipartFile image, @RequestParam String phoneNumber) {
		System.out.println("here");
		System.out.println(phoneNumber);
		Consommateur consommateur = consommateurService.getConsommateurByPhoneNumber(phoneNumber);
		String photoProfilFileName = phoneNumber + "_" + image.getOriginalFilename();
		Boolean isPhotoProfilUploaded = ImageIO.uploadImage(image, photoProfilFileName);
		if (isPhotoProfilUploaded == true && !image.isEmpty()) {
			consommateur.setUserPicture(photoProfilFileName);
			consommateurService.saveOrUpdateConsommateur(consommateur);
			return ResponseEntity.status(HttpStatus.OK).body(consommateur.getUserPicture());
		} else if (isPhotoProfilUploaded == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("INVALID FIELDS");

	}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
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

	@GetMapping(value = "/public/picture", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody
	byte[] getUserCINPicture(
			@RequestParam("phoneNumber") String phoneNumber
	) {

		/**
		 * http://localhost:8080/api/cinPicture?cinNumber=[cinNumber]
		 */
		if (DataValidationUtils.isValid(phoneNumber)) {
			System.out.println(phoneNumber);
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
