package com.onegateafrica.Controllers;


import com.onegateafrica.Controllers.utils.DataValidationUtils;
import com.onegateafrica.Controllers.utils.ImageIO;
import com.onegateafrica.Entities.*;
import com.onegateafrica.Payloads.request.SignUpRemorqueur;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class RemorqueurController {


    private static final int CIN_PHOTO_ID = 1;
    private static final int PATENTE_PHOTO_ID = 2;
    private static String imageDirectory = System.getProperty("user.dir") + "/images/";

    private final RemorqueurService remorqueurService;
    private final ConsommateurService consommateurService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    Logger logger = LoggerFactory.getLogger(RemorqueurController.class);


    @Autowired
    public RemorqueurController(RoleRepository roleRepository, RemorqueurService remorqueurService, ConsommateurService consommateurService,
                                BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.remorqueurService = remorqueurService;
        this.consommateurService = consommateurService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }
    @GetMapping("/getConsommateurAsRemorqueur/{idConsommateur}")
    public ResponseEntity<Object> getConsommateurAsRemorqueur(@PathVariable Long idConsommateur) {
        if(idConsommateur!= null) {
            Remorqueur remorqueur = remorqueurService.getConsommateurAsRemorqeur(idConsommateur).get();
            return ResponseEntity.status(HttpStatus.OK).body(remorqueur);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    //ajouté par radhwen ticket 1612
    @GetMapping("/remorqeur/{id}")
    //@PreAuthorize("hasRole('REMORQEUR')")
    public ResponseEntity<Remorqueur> getRemorqeurById(@PathVariable Long id) {
        if(id != null) {
            Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(id);
            if (remorqueur.get() != null) {
                return ResponseEntity.status(HttpStatus.OK).body(remorqueur.get());
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @GetMapping("/remorqeurpn")
    public ResponseEntity<Remorqueur> getRemorqeurByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        if(phoneNumber != null) {
            Optional<Remorqueur> remorqueur = remorqueurService.findRemorqueurByPhoneNumber(phoneNumber);
            if (remorqueur.isPresent()){
                return ResponseEntity.status(HttpStatus.OK).body(remorqueur.get());
            }

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }


    //ajouté par radhwen ticket 1612
    @PostMapping("/remorqeur/{remorqeurId}/{disponibility}")
    // @PreAuthorize("hasRole('REMORQEUR')")
    public ResponseEntity<Object> updateRemorqeur(@PathVariable Long remorqeurId, @PathVariable Boolean disponibility) {
        if (remorqeurId != null && disponibility != null) {
            Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(remorqeurId);
            if (remorqueur.get() != null) {
                try {
                    remorqueurService.updateDisponibility(remorqeurId, disponibility);

                    return ResponseEntity.status(HttpStatus.OK).body("succefully updated");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong");
                }
            } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(remorqueur.get());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please provide right path variables");

    }

    @PostMapping("/noterRemorqueur")
    public ResponseEntity<String> noterRemorqueur(
            @RequestParam("numeroCinRemorqueur") String numeroCinRemorqueur,
            @RequestParam("nombreEtoile") Integer nombreEtoile) {
        try {
            if (numeroCinRemorqueur == null && nombreEtoile == null) {
                return ResponseEntity.badRequest().body("ERROR");
            } else {
                Optional<Remorqueur> remorqueur = remorqueurService.findRemorqeurByCIN(numeroCinRemorqueur);
                if (remorqueur == null) {
                    return ResponseEntity.badRequest().body("Remoqueur not found");
                } else {
                    double nombreDeVoteAncien = remorqueur.get().getNombreDeVote();
                    double ancienNote = remorqueur.get().getNoteRemorqueurMoyenne();
                    double nouveauNote = (ancienNote * 1.0 + nombreEtoile * 1.0) / (nombreDeVoteAncien * 1.0);
                    System.out.println(nouveauNote);
                    remorqueur.get().setNoteRemorqueurMoyenne(nouveauNote);
                    remorqueur.get().setNombreDeVote(nombreDeVoteAncien + 1);
                    remorqueurService.saveOrUpdateRemorqueur(remorqueur.get());
                    return ResponseEntity.ok().body("SUCCESS");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Numero CIN invalide");
        }
    }

    @PostMapping("/uploadCinImage")
    public ResponseEntity<String> uploadCinImage(
            @RequestParam("cin") MultipartFile cin,
            @RequestParam("phoneNumber") String phoneNumber
    ) {
        if (ImageIO.uploadImage(cin, "cin-" + phoneNumber + "-" + cin.getOriginalFilename())) {
            return ResponseEntity.status(HttpStatus.CREATED).body("cin-" + phoneNumber + "-" + cin.getOriginalFilename());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");

    }

    @PostMapping("/uploadPatenteImage")
    public ResponseEntity<String> uploadPatenteImage(
            @RequestParam("patente") MultipartFile patente,
            @RequestParam("phoneNumber") String phoneNumber
    ) {
        if (ImageIO.uploadImage(patente, "patente" + "-" + phoneNumber + "-" + patente.getOriginalFilename())) {
            return ResponseEntity.status(HttpStatus.CREATED).body("patente-" + phoneNumber + "-" + patente.getOriginalFilename());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");

    }


    @PostMapping("/signupRemorqueurLibre")
    public ResponseEntity<String> saveRemorqueurLibre(@RequestBody SignUpRemorqueur body)
        {
            Consommateur consommateur= consommateurService.getConsommateurByPhoneNumber(body.getPhoneNumber());
            if(consommateur != null) {
                Remorqueur remorqueur = new Remorqueur();
                remorqueur.setConsommateur(consommateur);
                remorqueur.setCinNumber(body.getCinNumber());
                remorqueur.setCinPhoto(body.getCinPhoto());
                remorqueur.setDateDebut(new Date());
                remorqueur.setMatriculeRemorquage(body.getMatriculeRemorquage());
                remorqueur.setVerified(false);
                remorqueur.setRaisonSociale(body.getRaisonSociale());
                remorqueur.setRemorqeurType(RemorqeurType.LIBRE);
                remorqueur.setPatentePhoto(body.getPatentePhoto());
                remorqueurService.saveOrUpdateRemorqueur(remorqueur);
                return ResponseEntity.status(HttpStatus.CREATED).body("created");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

    @GetMapping(value = "/pictures", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getUserCINPicture(
            @RequestParam("cinNumber") String cinNumber,
            @RequestParam("imageType") Integer type
    ) {

        /**
         * http://localhost:8080/api/cinPicture?cinNumber=[cinNumber]
         */

        logger.info("fetching cinProfile Image");
        if (DataValidationUtils.isValid(cinNumber)) {
            Optional<Remorqueur> remorqueurlibre = remorqueurService.findRemorqeurByCIN(cinNumber);
            String imageName = null;
            switch (type) {
                case CIN_PHOTO_ID:
                    imageName = remorqueurlibre.get().getCinPhoto();
                    break;
                case PATENTE_PHOTO_ID:
                    imageName = remorqueurlibre.get().getPatentePhoto();
                    break;
                default:
                    imageName = null;
            }
            if (remorqueurlibre == null || imageName == null || imageName.isBlank()) {
                return ImageIO.getImagePlaceholder();
            } else {
                try {
                    byte[] image = ImageIO.getImage(imageName);
                    return image;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ImageIO.getImagePlaceholder();
                }
            }
        } else {
            return ImageIO.getImagePlaceholder();
        }

    }


    @GetMapping("/findAllRemorqueurLibre")
    public List<Remorqueur> getRemorqueurLibres() {
        return remorqueurService.getRemorqueurs();
    }

    @GetMapping("/findRemorqueurLibres/{id}")
    public Optional<Remorqueur> getRemorqueurLibre(@PathVariable Long id) {
        return remorqueurService.getRemorqueur(id);
    }

    @DeleteMapping("/deleteRemorqueurLibre/{id}")
    public void deleteRemorqueurLibre(@PathVariable Long id) {
        remorqueurService.deleteRemorqueur(id);

    }



}
