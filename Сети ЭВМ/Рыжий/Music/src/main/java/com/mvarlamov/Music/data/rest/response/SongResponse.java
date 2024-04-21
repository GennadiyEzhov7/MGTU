package com.mvarlamov.Music.data.rest.response;

import com.mvarlamov.Music.data.model.Song;
import lombok.Getter;

import java.util.List;

@Getter
public class SongResponse extends Response{
    List<Song> songs;
    public SongResponse(String message, boolean status, List<Song> songs) {
        super(message, status);
        this.songs = songs;
    }
}
