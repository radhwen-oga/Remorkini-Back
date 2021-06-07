package com.onegateafrica.Controllers;

import com.onegateafrica.Entities.Consommateur;
import com.onegateafrica.Payloads.request.LoginForm;
import com.onegateafrica.Payloads.response.JwtResponse;
import com.onegateafrica.Security.jwt.JwtUtils;
import com.onegateafrica.ServiceImpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.onegateafrica.Service.ConsommateurService;
import com.onegateafrica.Service.RemorqueurService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class ConversationController {
    private final JwtUtils jwtUtils;
    private final ConsommateurService consommateurService;
    private final RemorqueurService remorqueurService;

    @Autowired
    public ConversationController(ConsommateurService consommateurService, JwtUtils jwtUtils,
                                  RemorqueurService remorqueurService) {
        this.consommateurService = consommateurService;
        this.jwtUtils = jwtUtils;
        this.remorqueurService = remorqueurService;
    }


    @PostMapping("/sendMessage")
    public ResponseEntity<?> AuthenticatedUserRealm(@RequestHeader("accept-language") String language,
                                                    @RequestBody LoginForm loginRequest) {



        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NOT FOUND");
    }
}
