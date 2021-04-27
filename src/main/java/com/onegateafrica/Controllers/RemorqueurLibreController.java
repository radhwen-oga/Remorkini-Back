package com.onegateafrica.Controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.RemorqueurLibre;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurLibreService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RemorqueurLibreController {

	@Autowired
	ServletContext context;
	private static String imageDirectory = System.getProperty("user.dir") + "/images/";

	private final RemorqueurLibreService remorqueurLibreService;
	private final ConsommateurService consommateurService;
	@Autowired
	public RemorqueurLibreController(RemorqueurLibreService remorqueurLibreService,ConsommateurService consommateurService) {
		this.remorqueurLibreService = remorqueurLibreService;
		this.consommateurService=consommateurService;
	}

	private void makeDirectoryIfNotExist(String imageDirectory) {
		File directory = new File(imageDirectory);
		if (!directory.exists()) {
			directory.mkdir();
		}
	}

	@PostMapping("/signupRemorqueur/")
	public RemorqueurLibre saveRemorqueurLibre(@RequestParam("file") MultipartFile file,
			@RequestParam("remorqueur") String remorqueur)
			throws JsonParseException, JsonMappingException, IOException
	{
		RemorqueurLibre _remLibre = new ObjectMapper().readValue(remorqueur, RemorqueurLibre.class);

		makeDirectoryIfNotExist(imageDirectory);
		String filename = file.getOriginalFilename();
		Path fileNamePath = Paths.get(imageDirectory, filename.concat(".").concat(Files.getFileExtension(filename)));

		try {
			Files.write(file.getBytes(), new File(fileNamePath.toUri()));
			_remLibre.setCinPhoto(filename);
			_remLibre.setRemorqueur(true);
			remorqueurLibreService.saveRemorqueurLibre(_remLibre);

		} catch (IOException ex) {
			System.out.println(ex);
		}
		return _remLibre;
	}

	@GetMapping("/findAllRemorqueurLibre")
	public List<RemorqueurLibre> getRemorqueurLibres() {
		return remorqueurLibreService.getRemorqueurLibres();
	}

	@GetMapping("/findRemorqueurLibres/{id}")
	public Optional<RemorqueurLibre> getRemorqueurLibre(@PathVariable Long id) {
		return remorqueurLibreService.getRemorqueurLibre(id);
	}

	@DeleteMapping("/deleteRemorqueurLibre/{id}")
	public void deleteRemorqueurLibre(@PathVariable Long id) {
		remorqueurLibreService.deleteRemorqueurLibre(id);

	}

}
