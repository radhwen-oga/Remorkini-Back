package com.onegateafrica.Controllers;

import com.google.common.collect.Lists;
import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Entities.Conversation;
import com.onegateafrica.Entities.Message;
import com.onegateafrica.Security.jwt.JwtUtils;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.ConversationService;
import com.onegateafrica.Service.MessageService;
import com.onegateafrica.Service.RemorqueurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chat")
public class ConversationController {
    private final JwtUtils jwtUtils;
    private final ConsommateurService consommateurService;
    private final RemorqueurService remorqueurService;
    private final ConversationService conversationService;
    private final MessageService messageService;

    @Autowired
    public ConversationController(ConsommateurService consommateurService, JwtUtils jwtUtils,
                                  RemorqueurService remorqueurService, ConversationService conversationService, MessageService messageService) {
        this.consommateurService = consommateurService;
        this.jwtUtils = jwtUtils;
        this.remorqueurService = remorqueurService;
        this.conversationService = conversationService;
        this.messageService = messageService;
    }


    @PostMapping("/sendMessage")
    public ResponseEntity<?> SendMessage(@RequestHeader("Authorization") String auth,
                                         @RequestBody com.onegateafrica.Payloads.request.Message message) {
        if (message.getMessage() == null || message.getMessage().length() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid message");
        }
        else if(message.getReceiverId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("recieverId null");
        }
        String email = String.valueOf(jwtUtils.parseJwtToken(auth.substring(7)).getBody().get("sub"));
        Optional<Consommateur> consommateur1;
        Optional<Consommateur> consommateur2;
        Boolean isConsommateur = true;

            consommateur1 = consommateurService.getConsommateurByEmail(email);
            consommateur2 = consommateurService.getConsommateur(message.getReceiverId());
            if(consommateur1.isEmpty() || consommateur2.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid destination");
            }
            if (consommateur1.get().getId() == consommateur2.get().getId()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cannot send message to yourself");
            }

            Optional<Conversation> conversation = conversationService.findConversationByIds(consommateur1.get().getId(), consommateur2.get().getId());
            if (conversation.isEmpty()) {
                Conversation conversation1 = new Conversation();
                conversation1.setConsommateur1(consommateur1.get());
                conversation1.setConsommateur2(consommateur2.get());
                conversationService.save(conversation1);
                conversation = conversationService.findConversationByIds(consommateur1.get().getId(), consommateur2.get().getId());

            }
            Message newMessage = new Message();
            newMessage.setMessage(message.getMessage());
            newMessage.setTimestamp(new Date());
            newMessage.setSenderId(consommateur1.get().getId());
            newMessage.setConversation(conversation.get());
            newMessage.setSeen(false);
            newMessage.setReceived(false);
            newMessage.setRecieverId(consommateur2.get().getId());
            newMessage = messageService.save(newMessage);
            Conversation conversationUpdate = conversation.get();
            Date date = new Date();
            conversationUpdate.setLastActivity(date);
            conversationService.save(conversationUpdate);
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
    }

    @GetMapping("/getAllConversations")
    public ResponseEntity<?> AuthenticatedUserRealm(@RequestHeader("Authorization") String auth) {

        String email = String.valueOf(jwtUtils.parseJwtToken(auth.substring(7)).getBody().get("sub"));
        Optional<Consommateur> user = consommateurService.getConsommateurByEmail(email);
        System.out.println(user.isPresent());
        if (user.isPresent()) {
            Optional<List<Conversation>> conversations = conversationService.getAllConversation(user.get().getId());
            if (conversations.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(conversations.get());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT FOUND");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
    }

    @GetMapping("/getMessagesById/{id}/{begins}/{ends}")
    public ResponseEntity<?> AuthenticatedUserRealm(@PathVariable(name = "id") Long id,
                                                    @PathVariable(name = "begins") Integer begins,
                                                    @PathVariable(name = "ends") Integer ends,
                                                    @RequestHeader("Authorization") String auth) {
        System.out.println(begins + " " + ends + " " + id);
        if (!(begins != null && ends != null && id != null && begins >= 0 && ends >= begins)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Parameters");
        }
        String email = String.valueOf(jwtUtils.parseJwtToken(auth.substring(7)).getBody().get("sub"));
        Optional<Consommateur> user = consommateurService.getConsommateurByEmail(email);
        if (user.isPresent()) {
            System.out.println("user is present");
            Optional<Conversation> conversation = conversationService.findById(id);
            if (conversation.isPresent()) {
                System.out.println(user.get().getId() + " " + conversation.get().getConsommateur1().getId()
                        + " " + conversation.get().getConsommateur2().getId());
                if (user.get().getId() == conversation.get().getConsommateur1().getId() ||
                        user.get().getId() == conversation.get().getConsommateur2().getId()) {
                    Optional<List<Message>> messages = messageService.filterMessage(id);
                    if (messages.isPresent()) {
                        List<Message> messages1 = messages.get();
                        if (messages1.size() > ends + 1)
                            messages1 = messages1.subList(begins, ends + 1);
                        else
                            messages1 = messages1.subList(begins,messages1.size());
                        messages1 = Lists.reverse(messages1);
                        return ResponseEntity.status(HttpStatus.OK).body(messages1);
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no messages");
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no such conversation");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID");
    }

    @PostMapping("/setSeen/{convId}")
    public ResponseEntity<?> setSeen(@RequestHeader("Authorization") String auth,
                                         @PathVariable(name = "convId") Long convId) {
        if(convId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("null convId");
        }
        String email = String.valueOf(jwtUtils.parseJwtToken(auth.substring(7)).getBody().get("sub"));
        Optional<Consommateur> user = consommateurService.getConsommateurByEmail(email);
        if (user.isPresent()) {
            messageService.setSeen(convId,user.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body("done!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }
    @PostMapping("/setRecieved/{convId}")
    public ResponseEntity<?> SendMessage(@RequestHeader("Authorization") String auth,
                                         @PathVariable(name = "convId") Long convId) {
        if(convId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("null convId");
        }
        String email = String.valueOf(jwtUtils.parseJwtToken(auth.substring(7)).getBody().get("sub"));
        Optional<Consommateur> user = consommateurService.getConsommateurByEmail(email);
        if (user.isPresent()) {
            messageService.setRecieved(user.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body("done!");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong");
    }
}
