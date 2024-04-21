package com.mvarlamov.Music.data.rest.response;

import com.mvarlamov.Music.data.model.Artist;
import lombok.Getter;
import java.util.List;

@Getter
public class ArtistResponse extends Response {
    private final List<Artist> artists;

    public ArtistResponse(String message, boolean status, List<Artist> artists) {
        super(message, status);
        this.artists = artists;
    }
}
