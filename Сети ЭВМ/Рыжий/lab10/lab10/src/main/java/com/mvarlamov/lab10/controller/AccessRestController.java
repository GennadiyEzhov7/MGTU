package com.mvarlamov.lab10.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mvarlamov.lab10.dao.SessionDAO;
import com.mvarlamov.lab10.model.Role;
import com.mvarlamov.lab10.model.User;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@PreAuthorize("permitAll()")
public class AccessRestController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SessionDAO sessionDAO;

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/auth")
    public ResponseEntity<ObjectNode> auth(
            @NotNull @RequestParam String login,
            @NotNull @RequestParam String password
    ) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    password, login
                            )
                    );

            User user = (User) authenticate.getPrincipal();
            String token = UUID.randomUUID().toString();
            sessionDAO.createSession(user, token);

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION)
                    .body(mapper.createObjectNode()
                            .put("Token", token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/out")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<ObjectNode> logout(@RequestHeader("Authorization") String authHeader) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().contains(Role.ANONYMOUS))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    mapper.createObjectNode()
                            .put("status", "false")
                            .put("message", "Unable to logout anonymous user")
            );
        else {
            try {
                sessionDAO.removeSession(authHeader.substring(7));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        mapper.createObjectNode()
                                .put("status", "true")
                                .put("message", "Ok")
                );
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        mapper.createObjectNode()
                                .put("status", "false")
                                .put("message", "Session doesn't exist")
                );
            }
        }
    }
}
