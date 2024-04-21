package com.mvarlamov.Music.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Artist {
    private long id;
    private String name;
    private String description;
    private URI imagePath;
    private List<Song> songs;
    @JsonIgnore
    private MultipartFile image;
    @JsonIgnore
    @Autowired
    String hostPath;
    @JsonIgnore
    @Value("${imagesSaveFolder}")
    String saveFolder;

    public Artist setImage(MultipartFile image) {
        if (image == null)
            return this;
        this.image = image;
        this.imagePath = URI.create(saveFolder + image.getName());

        return this;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imagePath=" + imagePath +
                '}';
    }
}
