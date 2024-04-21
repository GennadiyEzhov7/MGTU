package com.mvarlamov.Music.data.dao;

import com.mvarlamov.Music.data.model.Artist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Component
public class ArtistDAO {
    @Autowired
    JdbcTemplate template;

    @Nullable
    public Artist getById(long id) {
        try {
            return template.queryForObject(
                    "SELECT * FROM ARTIST WHERE ID = ?",
                    artistRowMapper(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Artist> getByName(String name) {
        return template.query(
                "SELECT * FROM Artist WHERE Artist.name like ? OR Artist.name like ? OR Artist.name like ?",
                artistRowMapper(),
                String.format("%%%s%%", name),
                String.format("%s%%", name),
                String.format("%%%s", name)
        );
    }

    public List<Artist> getRandom(int max) {
        return template.query(
                "SELECT * FROM ARTIST WHERE ROWNUM <= ?",
                artistRowMapper(),
                max
        );
    }

    public void add(Artist artist) {
        template.update(
                "INSERT INTO ARTIST(name, description, imageName) VALUES (?,?,?)",
                artist.getName(),
                artist.getDescription(),
                artist.getImagePath() == null ? null : artist.getImagePath().toString()
        );
    }

    @Value("${imagesPath}")
    private String imagesPath;

    @Bean
    public RowMapper<Artist> artistRowMapper() {
        return (rs, rowNum) -> new Artist()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setDescription(rs.getString("description"))
                .setImagePath(URI.create(imagesPath + rs.getString("imageName")));
    }
}
