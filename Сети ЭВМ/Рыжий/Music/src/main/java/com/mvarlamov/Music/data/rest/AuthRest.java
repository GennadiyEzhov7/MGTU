package com.mvarlamov.Music.data.rest;

import com.mvarlamov.Music.data.dao.SessionDAO;
import com.mvarlamov.Music.data.dao.UserDAO;
import com.mvarlamov.Music.data.model.Action;
import com.mvarlamov.Music.data.model.Role;
import com.mvarlamov.Music.data.model.Session;
import com.mvarlamov.Music.data.model.User;
import com.mvarlamov.Music.data.rest.request.UserRequest;
import com.mvarlamov.Music.data.rest.response.ActionsResponse;
import com.mvarlamov.Music.data.rest.response.AuthResponse;
import com.mvarlamov.Music.data.rest.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class AuthRest {
    @Autowired
    AuthenticationManager authManager;

    @Autowired
    SessionDAO sessionDAO;

    @Autowired
    UserDAO userDAO;

    @GetMapping("/actions")
    public ResponseEntity<? extends Response> actions() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Action> actions = Stream.of(Action.FIND_SONGS, Action.FIND_ARTISTS).collect(Collectors.toList());

        if (user.getAuthorities().contains(Role.ADMIN)) {
            actions.addAll(Stream.of(Action.CREATE_ARTIST, Action.CREATE_SONG).collect(Collectors.toList()));
        }

        if (user.getAuthorities().contains(Role.USER)) {
            actions.addAll(Stream.of(Action.LOGOUT, Action.USER_PAGE).collect(Collectors.toList()));
        }

        if (user.getAuthorities().equals(Collections.singleton(Role.ANON))) {
            actions.addAll(Stream.of(Action.REGISTER, Action.LOGIN).collect(Collectors.toList()));
        }

        return ResponseEntity
                .ok(new ActionsResponse("Avaible actions", true, actions));
    }

    @PostMapping("/authorize")
    public ResponseEntity<? extends Response> authorize(
            @RequestParam String login,
            @RequestParam String password
    ) {
        Authentication authenticate = authManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                login, password
                        )
                );

        if (!authenticate.isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Response.of("Invalid login or password", false));
        }

        User user = (User) authenticate.getPrincipal();
        Session session = sessionDAO.create(user);

        return ResponseEntity
                .ok()
                .body(new AuthResponse("Authenticated", true, session.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<? extends Response> doRegister(@RequestBody UserRequest user) {
        if (user.getLogin() == null || user.getLogin().isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(Response
                            .of("Login must be not null or empty", false));
        if (user.getPassword() == null || user.getPassword().isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(Response
                            .of("Password must be not null or empty", false));

        if (userDAO.getByLogin(user.getLogin()) != null)
            return ResponseEntity
                    .badRequest()
                    .body(Response
                            .of("User with this login already exist", false));

        userDAO.create(user);

        return ResponseEntity
                .ok(Response
                        .of("User " + user.getName() + " created", true));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Response> logout() {
        sessionDAO.logout((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.err.println("!!!");
        return ResponseEntity
                .ok(Response.of("Ok", true));
    }
}
