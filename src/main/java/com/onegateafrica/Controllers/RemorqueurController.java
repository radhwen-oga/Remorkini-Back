package com.onegateafrica.Controllers;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.onegateafrica.Controllers.utils.DataValidationUtils;
import com.onegateafrica.Controllers.utils.ImageIO;
import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.ERole;
import com.onegateafrica.Entities.RemorqeurType;
import com.onegateafrica.Entities.Remorqueur;
import com.onegateafrica.Entities.Role;
import com.onegateafrica.Repositories.RoleRepository;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurService;

;import javax.persistence.PostUpdate;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/signUp")
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
    public RemorqueurController(RoleRepository roleRepository,RemorqueurService remorqueurService, ConsommateurService consommateurService,
                                BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.remorqueurService = remorqueurService;
        this.consommateurService = consommateurService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository=roleRepository;

    }




    @PostMapping("/signupRemorqueur")
    public ResponseEntity<String> saveRemorqueurLibre(
            @RequestParam("cinPhotoFile") MultipartFile cinPhotoFile,
            @RequestParam("patentePhotoFile") MultipartFile patentePhotoFile,
            @RequestParam("consommateurPhoneNumber") String consommateurPhoneNumber,
            @RequestParam("matriculeRemorquage") String matriculeRemorquage,
            @RequestParam("Activite") String Activite,
            @RequestParam("cinNumber") String cinNumber,
            @RequestParam("raisonSociale") String raisonSociale)
            throws JsonParseException, JsonMappingException, IOException {
        if (patentePhotoFile != null && cinPhotoFile != null) {
            if (DataValidationUtils.isValid(consommateurPhoneNumber)) {
                Consommateur consommateur = consommateurService.getConsommateurByPhoneNumber(consommateurPhoneNumber);
                if (consommateur != null) {
                    Optional<Remorqueur> remorqueur = remorqueurService.findRemorqeurByCIN(cinNumber);
                    if (remorqueur == null) {
                        if (DataValidationUtils.isValid(cinNumber) && DataValidationUtils.isValid(Activite) && DataValidationUtils.isValid(matriculeRemorquage)
                                && DataValidationUtils.isValid(raisonSociale)) {
                            String cinfilename = cinNumber + "_" + cinPhotoFile.getOriginalFilename();
                            String patenteFileName = cinNumber + "_" + patentePhotoFile.getOriginalFilename();
                            Boolean isCinImageUploaded = ImageIO.uploadImage(cinPhotoFile, cinfilename);
                            Boolean isPatenteImageUploaded = ImageIO.uploadImage(patentePhotoFile, patenteFileName);
                            if (isCinImageUploaded && isPatenteImageUploaded) {
                                Remorqueur remorqueurInsertable = new Remorqueur();




                                Optional<Role> role = roleRepository.findByRoleName(ERole.ROLE_REMORQUEUR);


                                System.out.println(consommateur.getRoles());

                                consommateur.getRoles().add(role.get());

                                System.out.println(consommateur.getRoles());

                                remorqueurInsertable.setCinNumber(cinNumber);
                                remorqueurInsertable.setActivite(Activite);
                                remorqueurInsertable.setMatriculeRemorquage(matriculeRemorquage);
                                remorqueurInsertable.setCinPhoto(cinfilename);
                                remorqueurInsertable.setPatentePhoto(patenteFileName);
                                remorqueurInsertable.setRaisonSociale(raisonSociale);
                                remorqueurInsertable.setRemorqeurType(RemorqeurType.LIBRE);
                                remorqueurInsertable.setConsommateur(consommateur);
                                consommateur.setRemorqueur(remorqueurInsertable);
                                remorqueurService.saveOrUpdateRemorqueur(remorqueurInsertable);
                                consommateurService.saveOrUpdateConsommateur(consommateur);
                                return ResponseEntity.status(HttpStatus.OK).body("Succeess");
                            } else {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while loading photos");
                            }

                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check states");
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Already Exists");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Consumer not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check phone number");
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Photos not loaded");
        }
    }


    @GetMapping(value = "/cinPicture", produces = MediaType.IMAGE_PNG_VALUE)
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

    //ajouté par radhwen ticket 1612
    @GetMapping("/remorqeur/{id}")
    //@PreAuthorize("hasRole('REMORQEUR')")
    public ResponseEntity< Remorqueur > getRemorqeurById (@PathVariable Long id) {
        Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(id);
        if(remorqueur.get() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(remorqueur.get());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(remorqueur.get());
    }
    //ajouté par radhwen ticket 1612
    @GetMapping("/remorqeur/{remorqeurId}/{disponibility}")
    // @PreAuthorize("hasRole('REMORQEUR')")
    public ResponseEntity<Object> updateRemorqeur (@PathVariable Long remorqeurId ,@PathVariable Boolean disponibility ){
        if(remorqeurId !=null && remorqeurId > 0 && disponibility!=null ){
            Optional<Remorqueur> remorqueur = remorqueurService.getRemorqueur(remorqeurId);
            if(remorqueur.get() != null){
                try {
                    remorqueurService.updateDisponibility(remorqeurId , disponibility);

                    return ResponseEntity.status(HttpStatus.OK).body("succefully updated");
                }
                catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("something went wrong");
                }
            }


            else  return ResponseEntity.status(HttpStatus.NOT_FOUND).body(remorqueur.get());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please provide right path variables");


    }



}
