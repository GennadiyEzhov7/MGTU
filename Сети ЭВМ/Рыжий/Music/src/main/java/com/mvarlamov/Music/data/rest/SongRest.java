package com.mvarlamov.Music.data.rest;

import com.mvarlamov.Music.data.dao.ArtistDAO;
import com.mvarlamov.Music.data.dao.SongDAO;
import com.mvarlamov.Music.data.model.Role;
import com.mvarlamov.Music.data.model.Song;
import com.mvarlamov.Music.data.model.User;
import com.mvarlamov.Music.data.rest.response.Response;
import com.mvarlamov.Music.data.rest.response.SongResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ANON')")
public class SongRest {
    @Autowired
    SongDAO songDAO;

    @Autowired
    ArtistDAO artistDAO;

    @Value("${songsSaveFolder}")
    String saveFolder;

    @PostMapping(value = "/create/song")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<? extends Response> createSong(
            @RequestPart("songData") Song song,
            @RequestPart("file") MultipartFile file
    ) {
        String errorMessage = null;
        if (song.getName() == null || song.getName().isEmpty())
            errorMessage = "Song name must not be null or empty";

        if (errorMessage == null && (song.getArtistId() == null || artistDAO.getById(song.getArtistId()) == null))
            errorMessage = "Artist with id=" + song.getArtistId() + " not found";

        if (errorMessage == null && (file == null || file.isEmpty()))
            errorMessage = "Song file is null or empty";

        if (errorMessage == null && (file.getContentType() == null || !file.getContentType().equals("audio/mpeg")))
            errorMessage = "File content type must be audio/mpeg";

        if (errorMessage == null && (file.getSize() > 20971520))
            errorMessage = "Song is too large, must be < 20MB";

        String fileName = song.getArtistId() + "_" + song.getName() + "_" + song.getAlbumId() + "_rnd" + (int) Math.floor(Math.random() * 100) + ".mp3";

        song.setFileName(fileName);
        songDAO.create(song);

        if (errorMessage != null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Response
                            .of(errorMessage, false));

        try {
            file.transferTo(new File(saveFolder + fileName));
            return ResponseEntity
                    .ok()
                    .body(Response
                            .of("Song " + song.getName() + " uploaded", true));
        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(Response
                            .of("Song " + song.getName() + " not loaded cause " + e.getMessage(), false));
        }
    }

    @GetMapping("/songs/{songName}")
    public ResponseEntity<? extends Response> getSongs(@PathVariable String songName) {
        return ResponseEntity.ok(new SongResponse(
                "List of songs by name=" + songName,
                true,
                songDAO.getByName(songName)
        ));
    }

    @GetMapping("/song/{songId}")
    public ResponseEntity<? extends Response> getSongs(@PathVariable int songId) {
        Song song = songDAO.getById(songId);
        if (song == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response
                            .of("Song with id=" + songId + " not found", false));

        return ResponseEntity.ok(new SongResponse(
                "Song by id=" + songId,
                true,
                Collections.singletonList(song)
        ));
    }

    @GetMapping("/songs")
    public ResponseEntity<? extends Response> getSongs() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(
                new SongResponse(
                        "Random tracks",
                        true,
                        songDAO.getRandom(5))
        );

    }
}
