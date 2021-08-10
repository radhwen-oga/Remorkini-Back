package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.ChatConversation;
import com.onegateafrica.Entities.ChatMessage;
import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.DemandeRemorquage;
import com.onegateafrica.Payloads.request.MessageDto;
import com.onegateafrica.Repositories.ChatConversationRepository;
import com.onegateafrica.Repositories.DemandeRemorquageRepository;
import com.onegateafrica.Service.ConsommateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chat")
public class ChatController {


    private final ConsommateurService consommateurService;

    private final DemandeRemorquageRepository demandeRemorquageRepository ;
    private final ChatConversationRepository chatConversationRepository ;


    @Autowired
    public ChatController(ConsommateurService consommateurService, DemandeRemorquageRepository demandeRemorquageRepository, ChatConversationRepository chatConversationRepository) {
        this.consommateurService = consommateurService;
        this.demandeRemorquageRepository = demandeRemorquageRepository;
        this.chatConversationRepository = chatConversationRepository;
    }



    @GetMapping("/getconversation/{idDemandeRemorquage}")
    public ResponseEntity<Object> creerConversation (@PathVariable Long idDemandeRemorquage) {

        if(idDemandeRemorquage !=null) {
            //try{
            DemandeRemorquage demandeRemorquage = demandeRemorquageRepository.findById(idDemandeRemorquage).get();
            return ResponseEntity.status(HttpStatus.OK).body(demandeRemorquage.getChatConversation());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("idDemande ne peut pas étre null");
    }


    @PostMapping("/ajouterMessage")
    public ResponseEntity<Object> ajouterMessageAuConversation (@RequestBody MessageDto messageDto){
        if(messageDto.getIdUser() != null && messageDto.getIdDemande() !=null && messageDto.getContenuMessage() !=null){
            DemandeRemorquage demandeRemorquage = demandeRemorquageRepository.findById(messageDto.getIdDemande()).get();
            Consommateur consommateur = consommateurService.getConsommateur(messageDto.getIdUser()).get();

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setContenu(messageDto.getContenuMessage());
            Instant now = Instant.now();
            Timestamp dateCreation = Timestamp.from(now);
            chatMessage.setDateCreation(dateCreation);

            chatMessage.setUser(consommateur);
            chatMessage.setConversation(demandeRemorquage.getChatConversation());
            demandeRemorquage.getChatConversation().getListeMessages().add(chatMessage);

            chatConversationRepository.save(demandeRemorquage.getChatConversation());

            return ResponseEntity.status(HttpStatus.OK).body(demandeRemorquage.getChatConversation().getListeMessages());

        }


        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("vérifiez les données");
    }



}
