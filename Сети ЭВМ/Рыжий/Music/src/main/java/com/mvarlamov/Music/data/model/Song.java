package com.mvarlamov.Music.data.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Song {
    private Integer id;
    private String name;
    private String genre;
    private Integer albumId;
    private Integer artistId;
    private String artistName;
    private String fileName;
    private boolean isLiked;
}
