package com.mvarlamov.Music.data.rest;

import com.mvarlamov.Music.data.dao.ArtistDAO;
import com.mvarlamov.Music.data.dao.SongDAO;
import com.mvarlamov.Music.data.model.Artist;
import com.mvarlamov.Music.data.model.Song;
import com.mvarlamov.Music.data.rest.response.ArtistResponse;
import com.mvarlamov.Music.data.rest.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAuthority('ANON')")
public class ArtistRest {
    @Autowired
    ArtistDAO artistDAO;

    @Autowired
    SongDAO songDAO;

    @GetMapping("/artist/{id}")
    public ResponseEntity<? extends Response> getArtist(@PathVariable int id) {
        Artist artist = artistDAO.getById(id);

        if (artist == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.of("Artist with id=" + id + " not found", false));

        artist.setSongs(songDAO.getArtistSongs(artist));

        return ResponseEntity
                .ok(new ArtistResponse(
                        "Artist info",
                        true,
                        Collections.singletonList(artist)
                ));
    }

    @GetMapping("/artists")
    public ResponseEntity<? extends Response> getArtists() {
        List<Artist> artistList = artistDAO.getRandom(5);
        return ResponseEntity
                .ok(new ArtistResponse("List of artists", true, artistList));
    }

    @GetMapping("/artists/{artistName}")
    public ResponseEntity<? extends Response> getArtistsByName(@PathVariable String artistName) {
        List<Artist> res = artistDAO.getByName(artistName.toLowerCase(Locale.ROOT));
        return ResponseEntity
                .ok(new ArtistResponse("List of artists by name=" + artistName, true, res));
    }

    @Value("${imagesSaveFolder}")
    String saveFolder;

    @PostMapping("/create/artist")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<? extends Response> createArtist(
            @RequestPart("artistData") Artist artist,
            @RequestPart("file") MultipartFile file
    ) {
        if (artist.getName() == null || artist.getName().isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(Response.of("Artist name must be not null or empty", false));

        String fileName = artist.getName() + "_rnd" + (int) Math.floor(Math.random() * 100) + ".jpeg";
        artist.setImagePath(URI.create(fileName));
        try {
            file.transferTo(new File(saveFolder + fileName));
            artistDAO.add(artist);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Response.of("Artist " + artist.getName() + " created", true));
        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(Response
                            .of("Artist " + artist.getName() + " not loaded cause " + e.getMessage(), false));

        }
    }
}
