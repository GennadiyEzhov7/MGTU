package com.mvarlamov.Music.data.rest;

import com.mvarlamov.Music.data.dao.SongDAO;
import com.mvarlamov.Music.data.dao.UserDAO;
import com.mvarlamov.Music.data.model.Song;
import com.mvarlamov.Music.data.model.User;
import com.mvarlamov.Music.data.rest.response.Response;
import com.mvarlamov.Music.data.rest.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('USER')")
public class UserRest {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SongDAO songDAO;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<? extends Response> getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response
                            .of("User not found", false));
        }

        List<Song> songs = songDAO.getUserLiked(user);

        return ResponseEntity
                .ok(new UserResponse("ok", true, user, songs));
    }

    @PostMapping("/like/{songId}")
    public ResponseEntity<? extends Response> likeSong(@PathVariable int songId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Song song = songDAO.getById(songId);

        if (song == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response
                            .of("Song not found", false));

        if (songDAO.getByIdAndUser(songId, user) != null)
            return ResponseEntity
                    .ok()
                    .body(Response.of("Already liked", true));

        songDAO.addToUserLiked(songId, user);

        return ResponseEntity
                .ok()
                .body(Response
                        .of("Song " + song.getName() + " has been liked", true));
    }

    @PostMapping("/unlike/{songId}")
    public ResponseEntity<? extends Response> unlikeSong(@PathVariable int songId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Song song = songDAO.getById(songId);

        if (song == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response
                            .of("Song not found", false));

        if (songDAO.getByIdAndUser(songId, user) == null)
            return ResponseEntity
                    .ok()
                    .body(Response.of("Song wasn't be liked", true));

        songDAO.removeFromUserLiked(songId, user);

        return ResponseEntity
                .ok()
                .body(Response
                        .of("Song " + song.getName() + " has been unliked", true));
    }
}
